package SPZXOrder.Task;

import SPZXOrder.Mapper.OrderInfoMapper;
import SPZXOrder.Mapper.OrderStatisticsMapper;
import cn.hutool.core.date.DateUtil;
import com.atguigu.spzx.model.entity.order.OrderStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author 清醒
 * @Date 2024/5/2 15:08
 */

@Component
@Slf4j
public class OrderStatisticsTask {
    @Autowired
    private OrderStatisticsMapper orderStatisticsMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    //注解Scheduled+cron表达式
    @Scheduled(cron = "0 0 1/24 1/1 * ? ")
    public void  test(){
        //获取前一天时间
        String createdate = DateUtil.offsetDay(new Date(), -1).toString("yyyy-mm-dd");

        //统计前一天时间的金额
        OrderStatistics orderStatistics=orderInfoMapper.selectOrderStatistics(createdate);

        if(orderStatistics!=null) {
            //将统计后的数据存入数据库中
            orderStatisticsMapper.insert(orderStatistics);
        }

    }
}
