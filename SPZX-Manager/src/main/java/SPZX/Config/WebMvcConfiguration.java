package SPZX.Config;

import SPZX.Interceptor.LoginAuthInterceptor;
import SPZX.Properties.UserAuthProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 解决跨域问题配置请求域名规则
 */
@Component
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    LoginAuthInterceptor loginAuthInterceptor;

    @Autowired
    UserAuthProperties userAuthProperties;



    //跨域配置文件
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")      // 添加路径规则
                .allowCredentials(true)               // 是否允许在跨域的情况下传递Cookie
                .allowedOriginPatterns("*")           // 允许请求来源的域规则
                .allowedMethods("*")
                .allowedHeaders("*");                // 允许所有的请求头
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginAuthInterceptor)
                //放行路径
                //读取配置文件和类中的路径
                 .excludePathPatterns(userAuthProperties.getNoAuthUrls())
//                .excludePathPatterns("/admin/system/index/login",
//                 "/admin/system/index/generateValidateCode")
                //拦截路径
                .addPathPatterns("/**");
    }

}