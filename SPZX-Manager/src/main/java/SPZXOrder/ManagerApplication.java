package SPZXOrder;

import SPZXOrder.Properties.Minioproperties;
import SPZXOrder.Properties.UserAuthProperties;
import Service.annotation.EnableLogAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync//异步
@EnableLogAspect//日志注解
@EnableScheduling//springTask定时任务
@EnableConfigurationProperties(value ={UserAuthProperties.class, Minioproperties.class} )//加载配置文件属性
public class ManagerApplication {

    public static void main(String[] args) {

        SpringApplication.run(ManagerApplication.class,args);

    }
}
