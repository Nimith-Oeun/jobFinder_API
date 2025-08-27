package persional.jobfinder_api.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import persional.jobfinder_api.dto.request.*;
import persional.jobfinder_api.exception.BadRequestException;
import persional.jobfinder_api.exception.InternalServerError;
import persional.jobfinder_api.exception.SuccessRespone;
import persional.jobfinder_api.model.UserProfile;
import persional.jobfinder_api.repository.UserProfileRepository;
import persional.jobfinder_api.service.ForgotPasswordService;
import persional.jobfinder_api.service.RegisterService;
import persional.jobfinder_api.service.UserService;
import persional.jobfinder_api.utils.JwtSecretUtil;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/jobfinder_api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final RegisterService registerService;
    private final UserProfileRepository userProfileRepository;
    private final ForgotPasswordService forgotPasswordService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        registerService.register(request);
        return ResponseEntity.ok(
                SuccessRespone.success("Registration successful! Please check your email for the verification code.")
        );
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOTPRequest request) {
        log.info("Verifying OTP for email: {}", request);
        if (registerService.verifyRegister(request)) {
            return ResponseEntity.ok(
                    SuccessRespone.success("Your OTP has been verified!")
            );
        }
        return ResponseEntity.badRequest()
                .body("Invalid OTP or OTP expired. Please try again.");
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestBody ResendOTPRequest request) {
        log.info("Resending OTP to email: {}", request.getEmail());
        registerService.resendOTP(request);
        return ResponseEntity.ok(
                SuccessRespone.success("OTP sent to your email!")
        );
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(JwtSecretUtil.getSecretKey())
                    .build()
                    .parseSignedClaims(refreshToken);

            Claims body = claims.getPayload();
            String username = body.getSubject();

            // Validate user exists
            userProfileRepository.findByUsername(username)
                    .orElseThrow(() -> new BadRequestException("User not found"));

            List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities", List.class);

            Set<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                    .map(x -> new SimpleGrantedAuthority(x.get("authority")))
                    .collect(Collectors.toSet());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    grantedAuthorities
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate new access token
            String newAccessToken = Jwts.builder()
                    .subject(username)
                    .issuedAt(new Date())
                    .claim("authorities", authorities)
                    .expiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000)) // 30 min
                    .issuer("jobfinder_api")
                    .signWith(JwtSecretUtil.getSecretKey())
                    .compact();

            Map<String, String> response = new HashMap<>();
            response.put("accessToken", newAccessToken);

            return ResponseEntity.ok(response);

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthenticationException("Refresh token expired") {});
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        forgotPasswordService.forgotPassword(request);
        return ResponseEntity.ok(
                SuccessRespone.success("Please Verify your email to reset your password!")
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        forgotPasswordService.resetPassword(request);
        return ResponseEntity.ok(
                SuccessRespone.success("Password reset successfully!")
        );
    }

}
