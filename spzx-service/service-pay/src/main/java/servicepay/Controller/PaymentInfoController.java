package servicepay.Controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import serviceorderclient.feign.OrderFeignClient;
import servicepay.Service.AlipayService;
import servicepay.Service.PaymentInfoService;
import servicepay.perprotis.AlipayProperties;

import java.util.Map;

/**
 * @Author 清醒
 * @Date 2024/5/9 19:45
 */
///api/order/alipay/callback/notify  支付宝回调路径

@RestController
@RequestMapping("/api/order/alipay")
@Slf4j
public class PaymentInfoController {

    @Autowired
    private AlipayService alipayService;
    @Autowired
    private AlipayProperties alipayProperties;
    @Autowired
    private OrderFeignClient orderFeignClient;
    @Autowired
    private PaymentInfoService paymentInfoService;

    @Operation(summary = "支付宝下单")
    @GetMapping("/submitAlipay/{orderNo}")
    public Result<String> submitAlipay(@Parameter(name = "orderNo", description = "订单号", required = true)
                                       @PathVariable(value = "orderNo") String orderNo) {
        String form = alipayService.submitAlipay(orderNo);
        return Result.build(form, ResultCodeEnum.SUCCESS);
    }


    @Operation(summary = "支付宝异步回调")
    @RequestMapping("/callback/notify")
    public String alipayNotify(@RequestParam Map<String, String> paramMap, HttpServletRequest request) {
        log.info("AlipayController...alipayNotify方法执行了...");
        boolean signVerified = false; //调用SDK验证签名
        try {
            signVerified = AlipaySignature.rsaCheckV1(paramMap, alipayProperties.getAlipayPublicKey(), AlipayProperties.charset, AlipayProperties.sign_type);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        // 交易状态
        String trade_status = paramMap.get("trade_status");

        // true
        if (signVerified) {

            //  验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
            if ("TRADE_SUCCESS".equals(trade_status) || "TRADE_FINISHED".equals(trade_status)) {
                // 正常的支付成功，我们应该更新交易记录状态
                paymentInfoService.updatePaymentStatus(paramMap, 2);
                return "success";
            }

        } else {
            //  验签失败则记录异常日志，并在response中返回failure.
            return "failure";
        }

        return "failure";
    }

}
