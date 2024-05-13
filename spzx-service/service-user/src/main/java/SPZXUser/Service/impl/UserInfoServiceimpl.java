package SPZXUser.Service.impl;

import SPZXOrder.Exception.GuiguException;
import SPZXUser.Mapper.UserInfoMapper;
import SPZXUser.Service.UserInfoService;
import SPZXOrder.Util.AuthContextUtil;
import com.alibaba.fastjson2.JSON;
import com.atguigu.spzx.model.dto.h5.UserLoginDto;
import com.atguigu.spzx.model.dto.h5.UserRegisterDto;
import com.atguigu.spzx.model.entity.user.UserInfo;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.h5.UserInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author 清醒
 * @Date 2024/5/5 14:28
 */
@Service
public class UserInfoServiceimpl implements UserInfoService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserInfoMapper userInfoMapper;


    //保存用户注册信息
    @Override
    public void register(UserRegisterDto userRegisterDto) {
        //从包装类中获取主要信息
        String code = userRegisterDto.getCode();
        String nickName = userRegisterDto.getNickName();
        String password = userRegisterDto.getPassword();
        //用户名即为手机号
        String username = userRegisterDto.getUsername();
        //从redis中查看是否存在验证码信息
        String rediscode = stringRedisTemplate.opsForValue().get(username);
        //将用户输入验证码与reids中验证码对比一致则放行
        if (!rediscode.equals(code)) {
            throw new GuiguException(ResultCodeEnum.VALIDATECODE_ERROR);
        }

        //校验用户名不能重复
        UserInfo userInfocompare = userInfoMapper.selectByUserName(username);
        if (userInfocompare != null) {
            throw new GuiguException(ResultCodeEnum.USER_NAME_IS_EXISTS);
        }
        //将用户数据封装存入数据库
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setNickName(nickName);
        userInfo.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        userInfo.setPhone(username);
        userInfo.setStatus(1);
        userInfo.setSex(0);
        userInfo.setAvatar("https://tse4-mm.cn.bing.net/th/id/OIP-C.MGxVQjnAFq5VvL0cJWx53QAAAA?rs=1&pid=ImgDetMain");
        userInfoMapper.save(userInfo);
        //从reids中删除验证码
        stringRedisTemplate.delete(username);

    }

    //用户登录
    @Override
    public String login(UserLoginDto userLoginDto) {
        //根据UserLoginDto获取用户信息用户名密码
        String password = userLoginDto.getPassword();
        String username = userLoginDto.getUsername();
        //根据用户名查询数据库
        UserInfo userInfo = userInfoMapper.selectByUserName(username);
        if (userInfo == null) {
            throw new GuiguException(ResultCodeEnum.LOGIN_ERROR);
        }
        //比较用户密码与输入密码是否一致
        //将密码转化为MD5
        String md5databasepassword = userInfo.getPassword();
        String md5password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5password.equals(md5databasepassword)) {
            throw new GuiguException(ResultCodeEnum.LOGIN_ERROR);
        }
        //判断用户是否处于禁用状态
        if (userInfo.getStatus() == 0) {
            throw new GuiguException(ResultCodeEnum.ACCOUNT_STOP);
        }
        //生成token
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        //将用户信息存放入redis(对象需要转化为json格式存储)
        stringRedisTemplate.opsForValue().set("user:" + token, JSON.toJSONString(userInfo), 7, TimeUnit.DAYS);

        // 返回token
        return token;

    }

    @Override
    public UserInfoVo getCurrentUserInfo(String token) {
        //通过token从redis中获取对象
//        String UserJson = stringRedisTemplate.opsForValue().get("user:" + token);
//        if(!StringUtils.hasText(UserJson)){
//            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
//        }

        //从threadlocal获取信息
        UserInfo userInfo = AuthContextUtil.getUserInfo();
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(userInfo, userInfoVo);
        return userInfoVo;//包含昵称与头像


    }
}
