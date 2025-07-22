/*
    * @OpenAPIDefinition used to define the OpenAPI specification for the application.
    * @SecurityScheme used to define the security scheme for the API.
    * @SecurityRequirement used to apply the security scheme to all endpoints.
    *Note: This configuration secures all endpoints globally when you used security with HTTP Basic Authentication.
 */

package persional.jobfinder_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
//@OpenAPIDefinition(
//        security = @SecurityRequirement(name = "basicAuth") // Secures all endpoints globally
//)
//@SecurityScheme(
//        name = "basicAuth", //Matches the name used in @SecurityRequirement
//        type = SecuritySchemeType.HTTP,
//        scheme = "basic" //Tells Swagger to use HTTP Basic Authentication
//)
public class SwaggerConfig {

    @Value("${server.port}")
    private String port;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                    .title("JobFinder API")
                    .version("1.0")
                    .description("API for Jobfinder application to manage job listings, applications, and user profiles."))
                .servers(new ArrayList<>(){{
                    add(new Server().url("http://localhost:" + port).description("Local HOST Dev"));
//                    add(new Server().url("http://localhost:8080/portfolio_API").description("Local server HOST"));
//                    add(new Server().url("http://192.168.100.100:8080/portfolio_API").description("Server"));
                }});

    }
}