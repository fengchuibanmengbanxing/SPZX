package SPZX.Interceptor;

import SPZX.AuthContextUtil;
import SPZX.Exception.GuiguException;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

@Component
public class LoginAuthInterceptor implements HandlerInterceptor {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求方式
        String method = request.getMethod();
        //判断请求方式是否为预检方式OPTIONS  是则直接放行
        if (method.equals("OPTIONS")) {
            return true;
        }
        //其他请求则进行拦截获取请求头token
        String token = request.getHeader("token");
        //判断token是否存在
        if(StrUtil.isEmpty(token)){
            responseNoLoginInfo(response);
            return false;
        }
        //带着请求头在redis中查找是否有登录信息
        String sysUserInfoJson = stringRedisTemplate.opsForValue().get("user:login:"+ token);
        //验证token合法性没有则提示用户登录
        if(StrUtil.isEmpty(sysUserInfoJson)){
            responseNoLoginInfo(response);
            return false;
        }
        //将用户数据存储至Threadlocal中
        SysUser sysUser = JSON.parseObject(sysUserInfoJson, SysUser.class);
        AuthContextUtil.set(sysUser);

        //修改reids中的过期时间为30分钟
        stringRedisTemplate.expire("user:login:"+ token,30, TimeUnit.MINUTES);
        //放行
        return true;
    }


    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        AuthContextUtil.remove();
    }


    //响应208状态码给前端
    private void responseNoLoginInfo(HttpServletResponse response) {
        Result<Object> result = Result.build(null, ResultCodeEnum.LOGIN_AUTH);
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(JSON.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) writer.close();
        }
    }
}
