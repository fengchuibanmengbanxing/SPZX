package servicepay;

import SPZXOrder.bean.EnableUserTokenFeignInterceptor;
import SPZXOrder.bean.EnableUserWebMvcConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import servicepay.perprotis.AlipayProperties;

/**
 * @Author 清醒
 * @Date 2024/5/9 12:47
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"serviceorderclient.feign", "SPZXOrder.feign"})
@EnableUserTokenFeignInterceptor
@EnableUserWebMvcConfiguration
@EnableConfigurationProperties(value = {AlipayProperties.class})
public class PayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class , args) ;
    }

}