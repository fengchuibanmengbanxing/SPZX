package SPZXOrder.Interceptor;

import SPZXOrder.Util.AuthContextUtil;
import com.alibaba.fastjson2.JSON;
import com.atguigu.spzx.model.entity.user.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @Author 清醒
 * @Date 2024/5/5 22:33
 */

//用户登录拦截器同时将用户token设置为专属变量
public class UserLoginAuthInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        String userInfoJSON = stringRedisTemplate.opsForValue().get("user:" + token);
        //设置静态专属数据
        AuthContextUtil.setUserInfo(JSON.parseObject(userInfoJSON , UserInfo.class));
        return true;
    }

}
