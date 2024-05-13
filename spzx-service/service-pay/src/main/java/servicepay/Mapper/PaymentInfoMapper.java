package servicepay.Mapper;

import com.atguigu.spzx.model.entity.pay.PaymentInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author 清醒
 * @Date 2024/5/9 18:51
 */
@Mapper
public interface PaymentInfoMapper {
    //根据orderNo编号查询订单支付记录
    PaymentInfo getPaymentInfo(String orderNo);
    //将订单信息加入paymentInfo
    void savePaymentInfo(PaymentInfo paymentInfo);
    //将更新后的状态写回数据库
    void updateById(PaymentInfo paymentInfo);
}
