package SPZXUser.Service.impl;

import SPZXUser.Mapper.UserAddressMapper;
import SPZXUser.Service.UserAddressService;
import SPZXOrder.Util.AuthContextUtil;
import com.atguigu.spzx.model.entity.user.UserAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/5/6 20:55
 */
@Service
public class UserAddressServiceimpl implements UserAddressService {
    @Autowired
    private UserAddressMapper userAddressMapper;

    //查询用户所有地址(根据用户idThreadlocal)
    @Override
    public List<UserAddress> findUserAddressList() {
        Long userId = AuthContextUtil.getUserInfo().getId();
        List<UserAddress> usersaddList=userAddressMapper.findByUserId(userId);

        return usersaddList;
    }

    @Override
    public UserAddress getUserAddress(Long id) {

        return userAddressMapper.getUserAddress(id);
    }
}
