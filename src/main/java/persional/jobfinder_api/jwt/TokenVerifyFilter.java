package persional.jobfinder_api.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class TokenVerifyFilter extends OncePerRequestFilter {

    @Value("${JWT_SECRETE_KEY}")
    String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (Objects.isNull(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.replace("Bearer ", "");

        try { // check if token expired then throw exception
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token);

            Claims body = claimsJws.getBody();
            String username = body.getSubject();
            List<Map<String,String>> authorities = (List<Map<String, String>>) body.get("authorities");

            Set<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                    .map(x -> new SimpleGrantedAuthority(x.get("authority")))
                    .collect(Collectors.toSet());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    grantedAuthorities
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (ExpiredJwtException e){
            log.info(e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

}
