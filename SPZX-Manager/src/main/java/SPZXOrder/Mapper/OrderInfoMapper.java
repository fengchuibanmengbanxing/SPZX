package SPZXOrder.Mapper;

import com.atguigu.spzx.model.entity.order.OrderStatistics;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author 清醒
 * @Date 2024/5/2 19:15
 */
@Mapper
public interface OrderInfoMapper {
    //统计前一天时间的金额
    OrderStatistics selectOrderStatistics(String createdate);
}
