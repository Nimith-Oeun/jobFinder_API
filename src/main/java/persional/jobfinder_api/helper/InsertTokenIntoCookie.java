package persional.jobfinder_api.helper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.time.Duration;
import java.util.stream.Stream;

public class InsertTokenIntoCookie {

    public static String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() == null) return null;

        return Stream.of(request.getCookies())
                .filter(cookie -> name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    public static void setTokensAsCookies(String accessToken,
                                    String refreshToken,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {

        boolean secure = request.isSecure(); // ✅ prod https=true, dev http=false

        // Access Token Cookie (15 min)
        ResponseCookie accessTokenCookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .secure(secure)
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .sameSite("Lax") // ✅ same-site recommended
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());

        // Refresh Token Cookie (7 days) - only sent to refresh endpoint
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(secure)
                .path("/jobfinder_api/v1/auth/refresh-token") // ✅ match your controller mapping
                .maxAge(Duration.ofDays(7))
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

}
