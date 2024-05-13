package servicepay.Service;

/**
 * @Author 清醒
 * @Date 2024/5/9 19:51
 */

public interface AlipayService {
    //支付宝下单
    String submitAlipay(String orderNo);
}
