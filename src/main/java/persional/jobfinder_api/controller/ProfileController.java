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
import persional.jobfinder_api.exception.SuccessRespone;
import persional.jobfinder_api.mapper.ProfileMapper;
import persional.jobfinder_api.repository.UserProfileRepository;
import persional.jobfinder_api.service.ForgotPasswordService;
import persional.jobfinder_api.service.RegisterService;
import persional.jobfinder_api.service.UserService;
import persional.jobfinder_api.utils.JwtSecretUtil;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/jobfinder_api/v1/profile")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

    private final UserService userService;


    //get current user profile that login
    @GetMapping("")
    public ResponseEntity<?> getCurrentUserProfile() {
        log.info("Get Method current user profile");
        try {

            return ResponseEntity.ok(
                    userService.getCurrentUserProfile()
            );

        } catch (Exception e) {

            log.error("Error fetching current user profile: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching the user profile.");

        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileUpdateRequest profileRequestDTO) {
        try {

            var updatedProfile = userService.updateUser(profileRequestDTO);
            return ResponseEntity.ok(
                    SuccessRespone.success(ProfileMapper.INSTANCE.mapToProfileRespone(updatedProfile))
            );

        } catch (BadRequestException e) {

            log.error("Bad request while updating profile: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());

        } catch (Exception e) {

            log.error("Error updating profile: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the profile.");

        }
    }


}
