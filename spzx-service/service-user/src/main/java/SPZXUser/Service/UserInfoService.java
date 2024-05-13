package SPZXUser.Service;

import com.atguigu.spzx.model.dto.h5.UserLoginDto;
import com.atguigu.spzx.model.dto.h5.UserRegisterDto;
import com.atguigu.spzx.model.vo.h5.UserInfoVo;

/**
 * @Author 清醒
 * @Date 2024/5/5 14:28
 */
public interface UserInfoService {

    //保存用户注册信息
    void register(UserRegisterDto userRegisterDto);

    //用户登录
    String login(UserLoginDto userLoginDto);
    //获取用户头像昵称
    UserInfoVo getCurrentUserInfo(String token);
}
