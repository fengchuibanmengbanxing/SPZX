package SPZXUser.Mapper;

import com.atguigu.spzx.model.entity.user.UserAddress;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/5/6 20:54
 */
@Mapper
public interface UserAddressMapper {

    //查询用户地址列表
    List<UserAddress> findByUserId(Long userId);
    //根据用户id查询收货信息
    UserAddress getUserAddress(Long id);
}
