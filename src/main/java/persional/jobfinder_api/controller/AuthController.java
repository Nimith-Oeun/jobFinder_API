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
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest requestBody) {

        try {
            // Parse & validate refresh token
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(JwtSecretUtil.getSecretKey())
                    .build()
                    .parseSignedClaims(requestBody.getRefreshToken());

            Claims body = claims.getPayload();
            String username = body.getSubject();

            log.info("Refreshing token for user: {}", username);

            // Validate user exists
            userProfileRepository.findByEmail(username)
                    .orElseThrow(() -> new BadRequestException("User not found"));

            // Get authorities from refresh token (optional, some designs only keep username in refresh token)
            List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities", List.class);
            Set<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                    .map(x -> new SimpleGrantedAuthority(x.get("authority")))
                    .collect(Collectors.toSet());

            // Generate new access token (30 min)
            String newAccessToken = Jwts.builder()
                    .subject(username)
                    .issuedAt(new Date())
                    .claim("authorities", authorities)
                    .expiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                    .issuer("jobfinder_api")
                    .signWith(JwtSecretUtil.getSecretKey())
                    .compact();

            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", newAccessToken);
            response.put("refreshToken", requestBody.getRefreshToken()); // send back the same refresh token
            response.put("authorities",
                    authorities.stream()
                            .map(x -> x.get("authority"))
                            .filter(auth -> auth.startsWith("ROLE_"))
                            .toArray(String[]::new)

            );

            return ResponseEntity.ok(response);

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of(
                            "status", 401,
                            "error", "Unauthorized",
                            "message", "Refresh token has expired. Please log in again.",
                            "path", "/refresh-token"
                    )
            );
        } catch (Exception e) {
           log.error("Error refreshing token: {}", e.getMessage());
            throw new InternalServerError("An error occurred while refreshing the token.");
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
