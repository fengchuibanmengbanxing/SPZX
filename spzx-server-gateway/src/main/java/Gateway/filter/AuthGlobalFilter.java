package Gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.spzx.model.entity.user.UserInfo;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/5/5 22:01
 */

/**登录状态校验*/
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求路径
        ServerHttpRequest request = exchange.getRequest();
        //判断路径是否符合/api/**/auth/**
        String path = request.getURI().getPath();
        UserInfo userInfo = this.getUserInfo(request);
        //api接口，异步请求，校验用户必须登录
        if(antPathMatcher.match("/api/**/auth/**", path)) {
            if(null == userInfo) {
                ServerHttpResponse response = exchange.getResponse();
                return out(response, ResultCodeEnum.LOGIN_AUTH);
            }
        }

        return chain.filter(exchange);
    }


    private Mono<Void> out(ServerHttpResponse response, ResultCodeEnum resultCodeEnum) {
        Result result = Result.build(null, resultCodeEnum);
        byte[] bits = JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }



    private UserInfo getUserInfo(ServerHttpRequest request) {
        String token="";
        //获取请求中的token集合
        List<String> tokenList = request.getHeaders().get("token");
        if(tokenList!=null){
             token = tokenList.get(0);
        }
        if(StringUtils.hasText(token)) {
            //根据token去redis中查询用户是否登录
            String UserInfoJSON = stringRedisTemplate.opsForValue().get("user:" + token);
            //redis中非空
            if (StringUtils.hasText(UserInfoJSON)) {
                UserInfo userInfo = JSON.parseObject(UserInfoJSON, UserInfo.class);
                return  userInfo;
            }else {
                return null;
            }
        }
       return null;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
