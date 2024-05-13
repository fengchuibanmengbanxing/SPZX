package SPZXOrder.Service;

import com.atguigu.spzx.model.dto.h5.OrderInfoDto;
import com.atguigu.spzx.model.entity.order.OrderInfo;
import com.atguigu.spzx.model.vo.h5.TradeVo;
import com.github.pagehelper.PageInfo;

/**
 * @Author 清醒
 * @Date 2024/5/7 19:49
 */
public interface OrderInfoService {
    //返回订单金额
    TradeVo getTrade();
    //提交订单
    Long submitOrder(OrderInfoDto orderInfoDto);
    //获取订单信息根据id
    OrderInfo getOrderInfo(Long orderId);
    //立即购买
    TradeVo buy(Long skuId);

    //我的订单查询展示
    PageInfo<OrderInfo> findUserPage(Integer page, Integer limit, Integer orderStatus);
    //远程调用获取订单信息
    OrderInfo getByOrderNo(String orderNo);
    //远程调用支付后更新订单信息
    void updateOrderStatus(String orderNo, Integer orderStatus);
}
