package SPZXOrder.Properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "spzx.auth")
@Data
public class UserAuthProperties {
private List<String> noAuthUrls;
}
