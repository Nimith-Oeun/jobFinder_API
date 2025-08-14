package persional.jobfinder_api.utils;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

public class JwtSecretUtil {

    // You can generate a secure random key once and paste here for production
    private static final String SECRET_STRING = "M2CzVp+fBlwimFGjVbZ9zWQ4wCuGz3qgGfqCSwzkjHY=";

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));

    public static SecretKey getSecretKey() {
        return SECRET_KEY;
    }
}
