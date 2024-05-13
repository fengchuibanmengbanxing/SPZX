package SPZXUser.Service;

import com.atguigu.spzx.model.entity.user.UserAddress;

import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/5/6 20:54
 */
public interface UserAddressService {
    //查询用户所有地址
    List<UserAddress> findUserAddressList();

    //根据用户id查询用户地址
    UserAddress getUserAddress(Long id);
}
