package servicepay.Service.Impl;

import SPZXOrder.feign.ProductFeignClient;
import com.alibaba.fastjson2.JSON;
import com.atguigu.spzx.model.dto.product.SkuSaleDto;
import com.atguigu.spzx.model.entity.order.OrderInfo;
import com.atguigu.spzx.model.entity.order.OrderItem;
import com.atguigu.spzx.model.entity.pay.PaymentInfo;
import com.atguigu.spzx.model.vo.common.Result;
import serviceorderclient.feign.OrderFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import servicepay.Mapper.PaymentInfoMapper;
import servicepay.Service.PaymentInfoService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author 清醒
 * @Date 2024/5/9 18:52
 */
@Service
public class PaymentInfoServiceimpl implements PaymentInfoService {
    @Autowired
    private PaymentInfoMapper paymentInfoMapper;
    //远程调用
    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private ProductFeignClient productFeignClient;
    //查询支付信息
    @Override
    public PaymentInfo savePaymentInfo(String orderNo) {
        //根据orderNo编号查询订单支付记录
        PaymentInfo paymentInfo = paymentInfoMapper.getPaymentInfo(orderNo);
        //检查记录是否为空
        if (paymentInfo == null) {
            //为空则将订单支付记录加入数据库
            paymentInfo = new PaymentInfo();
            OrderInfo orderInfo = new OrderInfo();
            //远程调用获取订单
            Result<OrderInfo> orderInfoByOrderNo = orderFeignClient.getOrderInfoByOrderNo(orderNo);
            orderInfo = orderInfoByOrderNo.getData();
            paymentInfo.setUserId(orderInfo.getUserId());
            paymentInfo.setPayType(orderInfo.getPayType());
            String content = "";
            for (OrderItem item : orderInfo.getOrderItemList()) {
                content += item.getSkuName() + " ";
            }
            paymentInfo.setContent(content);
            paymentInfo.setAmount(orderInfo.getTotalAmount());
            paymentInfo.setOrderNo(orderNo);
            paymentInfo.setPaymentStatus(0);
            //将订单信息加入paymentInfo
            paymentInfoMapper.savePaymentInfo(paymentInfo);
        }
        return paymentInfo;
    }

    //更新订单支付状态
    @Override
    public void updatePaymentStatus(Map<String, String> paramMap, int i) {
     //根据订单号查询订单
        PaymentInfo paymentInfo = paymentInfoMapper.getPaymentInfo(paramMap.get("out_trade_no"));

        //判断订单状态 已支付直接返回
        if(paymentInfo.getPayType()==1){
            return;
        }

        //更新支付信息
        paymentInfo.setPaymentStatus(1);
        paymentInfo.setOutTradeNo(paramMap.get("trade_no"));
        paymentInfo.setCallbackTime(new Date());
        paymentInfo.setCallbackContent(JSON.toJSONString(paramMap));
        paymentInfoMapper.updateById(paymentInfo);
        
        //更新订单状态
        orderFeignClient.updateOrderStatus(paymentInfo.getOrderNo());
        //远程调用修改数量
        // 1、查询PaymentInfo
        // 2、更新支付信息
        // 3、更新订单的支付状态
        // 4、更新商品销量
        Result<OrderInfo> orderInfoByOrderNo = orderFeignClient.getOrderInfoByOrderNo(paymentInfo.getOrderNo());
        //远程调用获取订单信息
        OrderInfo orderInfo = orderInfoByOrderNo.getData();
        //分解OrderItemList
        List<SkuSaleDto> skuSaleDtoList = orderInfo.getOrderItemList().stream().map(item -> {
            SkuSaleDto skuSaleDto = new SkuSaleDto();
            skuSaleDto.setSkuId(item.getSkuId());
            skuSaleDto.setNum(item.getSkuNum());
            return skuSaleDto;
        }).collect(Collectors.toList());
        productFeignClient.updateSkuSaleNum(skuSaleDtoList) ;
    }
}
