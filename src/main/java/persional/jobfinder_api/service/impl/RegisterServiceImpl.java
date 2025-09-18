package persional.jobfinder_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import persional.jobfinder_api.common.EmailService;
import persional.jobfinder_api.dto.request.ForgotPasswordRequest;
import persional.jobfinder_api.dto.request.RegisterRequest;
import persional.jobfinder_api.dto.request.ResendOTPRequest;
import persional.jobfinder_api.dto.request.VerifyOTPRequest;
import persional.jobfinder_api.enums.Role;
import persional.jobfinder_api.exception.BadRequestException;
import persional.jobfinder_api.exception.ResourNotFound;
import persional.jobfinder_api.model.UserProfile;
import persional.jobfinder_api.repository.UserProfileRepository;
import persional.jobfinder_api.service.RegisterService;
import persional.jobfinder_api.utils.HandleText;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final HandleText handleText;
    private final UserProfileRepository userProfileRepository;
    private final EmailService emailService;
    private final PasswordEncoder encrypPassword;

    private final int OTP_EXPIRY_MINUTES = 5;


    @Override
    public void register(RegisterRequest registerRequest) {

        handleText.HandleText(registerRequest.getUsername());
        handleText.HandleText(registerRequest.getEmail());

        if (userProfileRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new BadRequestException("userName already exists");
        }

        UserProfile userProfile = new UserProfile();
        userProfile.setUsername(registerRequest.getUsername());
        userProfile.setEmail(registerRequest.getEmail());
        userProfile.setPassword(encrypPassword.encode(registerRequest.getPassword())); // Ensure password is hashed in the UserProfile entity
        userProfile.setRole(Role.USER);
        userProfile.setAccountNonExpired(true);
        userProfile.setAccountNonLocked(true);
        userProfile.setCredentialsNonExpired(true);

        // Generate OTP
        String otp = String.format("%04d", new Random().nextInt(999999));
        userProfile.setOtp(otp);
        userProfile.setOtpExpiry(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));
        userProfile.setLastSendOtp(LocalDateTime.now());

        userProfileRepository.save(userProfile);

        // Send OTP email
        emailService.sendOtpEmail(registerRequest.getEmail(), otp);

    }

    @Override
    public boolean verifyRegister(VerifyOTPRequest verifyOTPRequest) {
        return userProfileRepository.findByEmail(verifyOTPRequest.getEmail())
                .filter(user -> user.getOtp().equals(verifyOTPRequest.getOtp()))
                .filter(user -> user.getOtpExpiry().isAfter(LocalDateTime.now()))
                .map(user -> {
                    user.setEnabled(true);
                    user.setOtpExpiry(null);
                    userProfileRepository.save(user);
                    return true;
                })
                .orElseThrow(() -> new BadRequestException("Invalid OTP or OTP expired"));
    }

    @Override
    public void resendOTP(ResendOTPRequest request) {

        UserProfile emailExist = userProfileRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourNotFound("Email not found"));

        if (emailExist.isEnabled()) {
            throw new BadRequestException("Account already verified");
        }


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

        // Send OTP email
        emailService.sendOtpEmail(request.getEmail(), otp);


    }
}
