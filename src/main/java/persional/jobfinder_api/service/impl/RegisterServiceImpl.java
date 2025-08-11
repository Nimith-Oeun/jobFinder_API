package persional.jobfinder_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import persional.jobfinder_api.common.EmailService;
import persional.jobfinder_api.dto.request.RegisterRequest;
import persional.jobfinder_api.dto.request.VerifyOTPRequest;
import persional.jobfinder_api.exception.BadRequestException;
import persional.jobfinder_api.model.UserProfile;
import persional.jobfinder_api.repository.UserProfileRepository;
import persional.jobfinder_api.service.RegisterService;
import persional.jobfinder_api.utils.HandleText;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final HandleText handleText;
    private final UserProfileRepository userProfileRepository;
    private final EmailService emailService;
    private final PasswordEncoder encrypPassword;


    @Override
    public void register(RegisterRequest registerRequest) {

        handleText.HandleText(registerRequest.getUsername());
        handleText.HandleText(registerRequest.getEmail());

        if (userProfileRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new BadRequestException("Email already exists");
        }

        UserProfile userProfile = new UserProfile();
        userProfile.setUsername(registerRequest.getUsername());
        userProfile.setEmail(registerRequest.getEmail());
        userProfile.setPassword(encrypPassword.encode(registerRequest.getPassword())); // Ensure password is hashed in the UserProfile entity

        // Generate OTP
        String otp = String.format("%04d", new Random().nextInt(999999));
        userProfile.setOtp(otp);
        userProfile.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

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
                    user.setActive(true);
                    user.setOtp(null);
                    user.setOtpExpiry(null);
                    userProfileRepository.save(user);
                    return true;
                })
                .orElse(false);
    }
}
