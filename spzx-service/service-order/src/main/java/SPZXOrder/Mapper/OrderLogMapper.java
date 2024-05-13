package SPZXOrder.Mapper;

import com.atguigu.spzx.model.entity.order.OrderLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author 清醒
 * @Date 2024/5/8 18:54
 */
@Mapper
public interface OrderLogMapper {
    //保存日志
    void save(OrderLog orderLog);
}
