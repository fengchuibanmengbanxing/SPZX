package SPZXOrder.Mapper;

import com.atguigu.spzx.model.entity.order.OrderItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/5/8 18:54
 */
@Mapper
public interface OrderItemMapper {
    //保存订单明细
    void save(OrderItem orderItem);
    //根据订单id查询订单信息
    List<OrderItem> findByOrderId(Long id);
}
