package SPZXOrder.Properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spzx.minio")
@Data
public class Minioproperties {

    private String endpointUrl;
    private String accessKey;
    private String secreKey;
    private String bucketName;

}
