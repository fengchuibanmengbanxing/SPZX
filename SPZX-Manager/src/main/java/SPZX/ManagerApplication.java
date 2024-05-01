package SPZX;

import SPZX.Properties.Minioproperties;
import SPZX.Properties.UserAuthProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value ={UserAuthProperties.class, Minioproperties.class} )//加载配置文件属性
public class ManagerApplication {

    public static void main(String[] args) {

        SpringApplication.run(ManagerApplication.class,args);

    }
}
