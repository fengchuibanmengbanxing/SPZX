package SPZXOrder;

import SPZXOrder.bean.EnableUserTokenFeignInterceptor;
import SPZXOrder.bean.EnableUserWebMvcConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author 清醒
 * @Date 2024/5/6 21:00
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"SPZXOrder","SPZXUSERClient.Fiegn","SPZXCartclient.feign"})
@EnableUserTokenFeignInterceptor
@EnableUserWebMvcConfiguration
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class , args) ;
    }

}