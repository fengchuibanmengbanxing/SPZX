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
@EnableFeignClients(basePackages = {"serviceorderclient.feign", "SPZXOrder.feign"})//远程调用
@EnableUserTokenFeignInterceptor//获取登录token
@EnableUserWebMvcConfiguration //路径拦截
@EnableConfigurationProperties(value = {AlipayProperties.class})//导入配置文件
public class PayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class , args) ;
    }

}