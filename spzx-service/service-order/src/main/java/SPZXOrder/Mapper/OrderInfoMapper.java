package SPZXOrder.Mapper;

import com.atguigu.spzx.model.entity.order.OrderInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/5/8 17:11
 */
@Mapper
public interface OrderInfoMapper {
    //保存订单信息
    void save(OrderInfo orderInfo);
   //获取订单信息根据id
    OrderInfo getOrderInfo(Long orderId);
   //根据状态查询订单
    List<OrderInfo> getOrderById(Long userId, Integer orderStatus);
    //远程调用获取订单信息
    OrderInfo getByOrderNo(String orderNo);
    //更新订单状态
    void updateById(OrderInfo orderInfo);
}
