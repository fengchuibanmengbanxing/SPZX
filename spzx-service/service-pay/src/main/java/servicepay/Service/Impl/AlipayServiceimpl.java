package servicepay.Service.Impl;

import SPZXOrder.Exception.GuiguException;
import com.alibaba.fastjson2.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.atguigu.spzx.model.entity.pay.PaymentInfo;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import servicepay.Service.AlipayService;
import servicepay.Service.PaymentInfoService;
import servicepay.perprotis.AlipayProperties;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @Author 清醒
 * @Date 2024/5/9 19:51
 */
@Service
@Slf4j
public class AlipayServiceimpl implements AlipayService {

    @Autowired
    private PaymentInfoService paymentInfoService;
    @Autowired
    private AlipayProperties alipayProperties;


    @Autowired
    private AlipayClient alipayClient;
    //支付宝下单
    @Override
    public String submitAlipay(String orderNo) {
        //保存支付记录
        PaymentInfo paymentInfo = paymentInfoService.savePaymentInfo(orderNo);

        //调用支付宝接口支付
        //创建API对应的request
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();

        //支付之后返回支付成功或失败界面
        // 同步回调
        alipayRequest.setReturnUrl(alipayProperties.getReturnPaymentUrl());
        //改变支付，商品销量，订单状态(有并发问题暂不考虑)
        // 异步回调
        alipayRequest.setNotifyUrl(alipayProperties.getNotifyPaymentUrl());

        // 准备请求参数 ，声明一个map 集合
        HashMap<String, Object> map = new HashMap<>();
        map.put("out_trade_no", paymentInfo.getOrderNo());
        map.put("product_code", "QUICK_WAP_WAY");
        //map.put("total_amount",paymentInfo.getAmount());
        map.put("total_amount", new BigDecimal("0.01"));
        map.put("subject", paymentInfo.getContent());
        alipayRequest.setBizContent(JSON.toJSONString(map));

        // 发送请求
        AlipayTradeWapPayResponse response = null;
        try {
            response = alipayClient.pageExecute(alipayRequest);
            if (response.isSuccess()) {
                log.info("调用成功");
                return response.getBody();
            } else {
                log.info("调用失败");
                throw new GuiguException(ResultCodeEnum.DATA_ERROR);
            }
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }

    }




}

