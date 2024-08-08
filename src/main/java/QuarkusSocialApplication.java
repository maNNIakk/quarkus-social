
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

@OpenAPIDefinition(
        info = @Info(
                title = "Quarkus Social Backend",
                version = "1.0",
                description = "API for managing social media posts and " +
                        "followers",
                contact = @Contact(
                        name = "Renato Santos",
                        email = "renato.yancovit3@gmail.com",
                        url = "https://github.com/maNNIakk/quarkus-social"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        )
)
public class QuarkusSocialApplication extends Application {

}
