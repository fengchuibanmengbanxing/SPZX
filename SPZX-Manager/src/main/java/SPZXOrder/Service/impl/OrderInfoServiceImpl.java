package SPZXOrder.Service.impl;

import SPZXOrder.Mapper.OrderStatisticsMapper;
import SPZXOrder.Service.OrderInfoService;
import cn.hutool.core.date.DateUtil;
import com.atguigu.spzx.model.dto.order.OrderStatisticsDto;
import com.atguigu.spzx.model.entity.order.OrderStatistics;
import com.atguigu.spzx.model.vo.order.OrderStatisticsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 清醒
 * @Date 2024/5/3 11:33
 */
@Service
public class OrderInfoServiceImpl implements OrderInfoService {

    @Autowired
    private OrderStatisticsMapper orderStatisticsMapper;

    //统计订单数据
    @Override
    public OrderStatisticsVo getOrderStatisticsData(OrderStatisticsDto orderStatisticsDto) {
        //根据条件查询对应数据
        List<OrderStatistics> orderStatisticslst = orderStatisticsMapper.selectlist(orderStatisticsDto);
        //将数据提取日期进行分解存入新的list集合(使用stream流的方式)
        List<String> datelist = orderStatisticslst.stream().map(orderStatistics ->
                //将日期数据转化固定的格式
                DateUtil.format(orderStatistics.getOrderDate(), "yyyy-MM-dd")
        ).collect(Collectors.toList());
        //将金额数据提取存入新的list集合
        //统计金额列表
        List<BigDecimal> amountList = orderStatisticslst.stream().
                map(OrderStatistics::getTotalAmount).
                collect(Collectors.toList());

        //返回包装数据
        OrderStatisticsVo orderStatisticsVo = new OrderStatisticsVo();
        orderStatisticsVo.setDateList(datelist);
        orderStatisticsVo.setAmountList(amountList);
        return orderStatisticsVo;

    }
}
