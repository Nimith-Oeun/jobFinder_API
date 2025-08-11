package persional.jobfinder_api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import persional.jobfinder_api.dto.request.RegisterRequest;
import persional.jobfinder_api.dto.request.VerifyOTPRequest;
import persional.jobfinder_api.exception.SuccessRespone;
import persional.jobfinder_api.service.RegisterService;

@RestController
@RequestMapping("/jobfinder_api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        registerService.register(request);
        return ResponseEntity.ok("OTP sent to your email!");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOTPRequest request) {
        log.info("Verifying OTP for email: {}", request);
        if (registerService.verifyRegister(request)) {
            return ResponseEntity.ok(SuccessRespone.success("Account verified successfully!"));
        }
        return ResponseEntity.badRequest()
                .body("Invalid OTP or OTP expired. Please try again.");
    }
}
