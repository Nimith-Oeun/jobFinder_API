package persional.jobfinder_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import persional.jobfinder_api.common.EmailService;
import persional.jobfinder_api.dto.request.ForgotPasswordRequest;
import persional.jobfinder_api.dto.request.ResetPasswordRequest;
import persional.jobfinder_api.exception.InternalServerError;
import persional.jobfinder_api.exception.ResourNotFound;
import persional.jobfinder_api.model.PasswordResetToken;
import persional.jobfinder_api.model.UserProfile;
import persional.jobfinder_api.repository.ResetPassowdTokenRepository;
import persional.jobfinder_api.repository.UserProfileRepository;
import persional.jobfinder_api.service.ForgotPasswordService;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private final UserProfileRepository userProfileRepository;
    private final EmailService emailService;
    private final ResetPassowdTokenRepository resetPassowdTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final int OTP_EXPIRY_MINUTES = 5;


    @Override
    public void forgotPassword(ForgotPasswordRequest email) {

            UserProfile emailExist = userProfileRepository.findByEmail(email.getEmail())
                    .orElseThrow(() -> new ResourNotFound("Email not found"));

            int RESEND_COOLDOWN_SECONDS = 180;

            // Check if the last OTP was sent within the cooldown period
            if (Objects.nonNull(emailExist.getLastSendOtp()) && emailExist.getLastSendOtp().plusSeconds(RESEND_COOLDOWN_SECONDS).isAfter(LocalDateTime.now())) {
                throw new RuntimeException("Please wait before resending OTP");

            }

            // Generate new OTP
            String otp = String.format("%04d", new Random().nextInt(999999));
            emailExist.setOtp(otp);
            emailExist.setOtpExpiry(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));
            emailExist.setLastSendOtp(LocalDateTime.now());
            userProfileRepository.save(emailExist);

            PasswordResetToken reset = new PasswordResetToken();
            reset.setOtp(otp);
            reset.setExpiryTime(emailExist.getOtpExpiry());
            reset.setEmail(email.getEmail());
            resetPassowdTokenRepository.save(reset);

            // Track last send time in user profile (optional)
            emailExist.setLastSendOtp(LocalDateTime.now());
            userProfileRepository.save(emailExist);

            // Send OTP email
            emailService.sendOtpEmail(email.getEmail(), otp);
    }

    @Override
    public void resetPassword(ResetPasswordRequest reset) {

        // Find the user by email
        PasswordResetToken getUser = resetPassowdTokenRepository.findByEmail(reset.getEmail())
                .orElseThrow(() -> new ResourNotFound("Email not found"));

        UserProfile emailExist = userProfileRepository.findByEmail(reset.getEmail())
                .orElseThrow(() -> new ResourNotFound("Email not found"));

        // Check if the OTP is valid`
        if (!emailExist.getOtp().equals(getUser.getOtp()) || getUser.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new InternalServerError("Invalid OTP or OTP expired");
        }

        // Update the user's password
        emailExist.setPassword(passwordEncoder.encode(reset.getNewPassword()));
        userProfileRepository.save(emailExist);

        // Save the updated user profile
        resetPassowdTokenRepository.delete(getUser);


    }

}
