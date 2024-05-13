package SPZXOrder.Service;

import com.atguigu.spzx.model.dto.order.OrderStatisticsDto;
import com.atguigu.spzx.model.vo.order.OrderStatisticsVo;

/**
 * @Author 清醒
 * @Date 2024/5/3 11:34
 */
public interface OrderInfoService {
    //统计订单数据
    OrderStatisticsVo getOrderStatisticsData(OrderStatisticsDto orderStatisticsDto);
}
