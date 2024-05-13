package SPZXUser.Mapper;

import com.atguigu.spzx.model.entity.user.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author 清醒
 * @Date 2024/5/5 14:28
 */
@Mapper
public interface UserInfoMapper {
    //根据用户名查询用户防止重复用户名
    UserInfo selectByUserName(String username);
    //保存注册信息
    void save(UserInfo userInfo);

}
