package synrgy.team4.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
//    @Value("${openapi.dev-url}")
//    private String devUrl;

    @Value("${openapi.prod-url}")
    private String prodUrl;

    @Bean
    public OpenAPI myOpenAPI() {
//        Server devServer = new Server();
//        devServer.setUrl(devUrl);
//        devServer.setDescription("Server URL in Development environment");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production environment");

        Contact contact = new Contact();
        contact.setEmail("synrgyteam4@gmail.com");
        contact.setName("Team 4");
//        contact.setUrl("https://firmanpu.tech");

        License mitLicense = new License().name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Lumi Backend Java API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to Lumi by Team 4 for the Final Project from Synrgy Academy.")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(prodServer));
    }
}