package servicepay.Service;

import com.atguigu.spzx.model.entity.pay.PaymentInfo;

import java.util.Map;

/**
 * @Author 清醒
 * @Date 2024/5/9 18:51
 */
public interface PaymentInfoService {
    PaymentInfo savePaymentInfo(String orderNo);
//更新支付状态
    void updatePaymentStatus(Map<String, String> paramMap, int i);
}
