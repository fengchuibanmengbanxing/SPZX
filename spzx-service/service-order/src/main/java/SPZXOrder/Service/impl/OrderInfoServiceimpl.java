package SPZXOrder.Service.impl;

import SPZXOrder.Exception.GuiguException;
import SPZXOrder.Mapper.OrderInfoMapper;
import SPZXOrder.Mapper.OrderItemMapper;
import SPZXOrder.Mapper.OrderLogMapper;
import SPZXOrder.Util.AuthContextUtil;
import SPZXCartclient.feign.CartFeignClient;
import SPZXOrder.Service.OrderInfoService;
import SPZXOrder.feign.ProductFeignClient;
import SPZXUSERClient.Fiegn.UserFeignClient;
import com.atguigu.spzx.model.dto.h5.OrderInfoDto;
import com.atguigu.spzx.model.entity.h5.CartInfo;
import com.atguigu.spzx.model.entity.order.OrderInfo;
import com.atguigu.spzx.model.entity.order.OrderItem;
import com.atguigu.spzx.model.entity.order.OrderLog;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.atguigu.spzx.model.entity.user.UserAddress;
import com.atguigu.spzx.model.entity.user.UserInfo;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.h5.TradeVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/5/7 19:50
 */
@Service
public class OrderInfoServiceimpl implements OrderInfoService {

    @Autowired
    private CartFeignClient cartFeignClient;
    @Autowired
    private ProductFeignClient productFeignClient;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderLogMapper orderLogMapper;


    @Override
    public TradeVo getTrade() {
        TradeVo tradeVo = new TradeVo();
        //选中商品集合
        List<CartInfo> allCkeckedList = cartFeignClient.getAllCkecked();
        ArrayList<OrderItem> OrderItemList = new ArrayList<>();
        for (CartInfo cartInfo : allCkeckedList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setSkuId(cartInfo.getSkuId());
            orderItem.setSkuName(cartInfo.getSkuName());
            orderItem.setSkuNum(cartInfo.getSkuNum());
            orderItem.setSkuPrice(cartInfo.getCartPrice());
            orderItem.setThumbImg(cartInfo.getImgUrl());
            OrderItemList.add(orderItem);
        }
        //计算选中商品金额
        BigDecimal bigDecimal = new BigDecimal(0);
        for (OrderItem orderItem : OrderItemList) {
            //获取单价与数量
            BigDecimal skuPrice = orderItem.getSkuPrice();
            Integer skuNum = orderItem.getSkuNum();
            //计算总金额
            BigDecimal multiply = skuPrice.multiply(new BigDecimal(skuNum));
            bigDecimal = bigDecimal.add(multiply);
        }
        tradeVo.setTotalAmount(bigDecimal);
        tradeVo.setOrderItemList(OrderItemList);
        return tradeVo;
    }

    @Override
    public Long submitOrder(OrderInfoDto orderInfoDto) {
        //从OrderInfoDto对象中获取orderItemList集合
        List<OrderItem> orderItemList = orderInfoDto.getOrderItemList();
        //校验orderItemList集合是否为空
        if (CollectionUtils.isEmpty(orderItemList)) {
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }
        //检验商品库存是否充足
        //遍历集合
        for (OrderItem orderItem : orderItemList) {
            //遍历orderItemList集合根据skuId每个商品全部查看库存
            Long skuId = orderItem.getSkuId();
            //调用远程接口查询数据库
            ProductSku productSku = productFeignClient.getBySkuId(skuId);
            if (productSku == null) {
                throw new GuiguException(ResultCodeEnum.DATA_ERROR);
            }
            //库存不足抛出异常
            if (orderItem.getSkuNum() > productSku.getSaleNum()) {
                throw new GuiguException(ResultCodeEnum.STOCK_LESS);
            }
        }

        // 构建订单数据，保存订单
        UserInfo userInfo = AuthContextUtil.getUserInfo();
        OrderInfo orderInfo = new OrderInfo();
        //订单编号
        orderInfo.setOrderNo(String.valueOf(System.currentTimeMillis()));
        //用户id
        orderInfo.setUserId(userInfo.getId());
        //用户昵称
        orderInfo.setNickName(userInfo.getNickName());
        //用户收货地址信息
        UserAddress userAddress = userFeignClient.getUserAddress(orderInfoDto.getUserAddressId());
        orderInfo.setReceiverName(userAddress.getName());
        orderInfo.setReceiverPhone(userAddress.getPhone());
        orderInfo.setReceiverTagName(userAddress.getTagName());
        orderInfo.setReceiverProvince(userAddress.getProvinceCode());
        orderInfo.setReceiverCity(userAddress.getCityCode());
        orderInfo.setReceiverDistrict(userAddress.getDistrictCode());
        orderInfo.setReceiverAddress(userAddress.getFullAddress());
        //订单金额
        BigDecimal totalAmount = new BigDecimal(0);
        for (OrderItem orderItem : orderItemList) {
            totalAmount = totalAmount.add(orderItem.getSkuPrice().multiply(new BigDecimal(orderItem.getSkuNum())));
        }
        orderInfo.setTotalAmount(totalAmount);
        orderInfo.setCouponAmount(new BigDecimal(0));
        orderInfo.setOriginalTotalAmount(totalAmount);
        orderInfo.setFeightFee(orderInfoDto.getFeightFee());
        orderInfo.setPayType(2);
        orderInfo.setOrderStatus(0);
        orderInfoMapper.save(orderInfo);

        //保存订单明细
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderId(orderInfo.getId());
            orderItemMapper.save(orderItem);
        }

        //记录日志
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(orderInfo.getId());
        orderLog.setProcessStatus(0);
        orderLog.setNote("提交订单");
        orderLogMapper.save(orderLog);
        // TODO 远程调用service-cart微服务接口清空购物车数据
        cartFeignClient.deleteChecked();

        return orderInfo.getId();
    }

    //获取订单信息根据id
    @Override
    public OrderInfo getOrderInfo(Long orderId) {
        OrderInfo orderInfo = orderInfoMapper.getOrderInfo(orderId);
        return orderInfo;
    }

    @Override
    public TradeVo buy(Long skuId) {
        List<OrderItem> orderItemList = new ArrayList<>();
        TradeVo tradeVo = new TradeVo();
        ProductSku productSku = productFeignClient.getBySkuId(skuId);
        BigDecimal bigDecimal = productSku.getCostPrice();
        tradeVo.setTotalAmount(bigDecimal);
        OrderItem orderItem = new OrderItem();
        //设置商品信息
        orderItem.setSkuId(skuId);
        orderItem.setSkuName(productSku.getSkuName());
        orderItem.setSkuNum(1);
        orderItem.setSkuPrice(productSku.getSalePrice());
        orderItem.setThumbImg(productSku.getThumbImg());
        orderItemList.add(orderItem);
        tradeVo.setOrderItemList(orderItemList);


        return tradeVo;

    }

    @Override
    public PageInfo<OrderInfo> findUserPage(Integer page, Integer limit, Integer orderStatus) {
        PageHelper.startPage(page, limit);
        //根据用户id查询用户所有id(除去被逻辑删除的)
        UserInfo userInfo = AuthContextUtil.getUserInfo();
        Long userId = userInfo.getId();
        //根据状态筛选订单
        List<OrderInfo> orderInfoList = orderInfoMapper.getOrderById(userId, orderStatus);
        //获取订单的详细信息
        orderInfoList.stream().forEach(orderInfo -> {
            List<OrderItem> orderItem = orderItemMapper.findByOrderId(orderInfo.getId());
            orderInfo.setOrderItemList(orderItem);
        });

        return new PageInfo<>(orderInfoList);
    }


    //远程调用获取订单信息
    @Override
    public OrderInfo getByOrderNo(String orderNo) {
        OrderInfo orderInfo=orderInfoMapper.getByOrderNo(orderNo);
        List<OrderItem> orderItem = orderItemMapper.findByOrderId(orderInfo.getId());
        orderInfo.setOrderItemList(orderItem);
        return orderInfo;
    }


    //远程调用支付后更新订单信息
    @Override
    public void updateOrderStatus(String orderNo, Integer orderStatus) {

        //根据订单编号获取订单
        OrderInfo orderInfo = orderInfoMapper.getByOrderNo(orderNo);
        orderInfo.setOrderStatus(1);
        orderInfo.setPayType(orderStatus);
        orderInfo.setPaymentTime(new Date());
        //更新订单状态
        orderInfoMapper.updateById(orderInfo);
        // 记录日志
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(orderInfo.getId());
        orderLog.setProcessStatus(1);
        orderLog.setNote("支付宝支付成功");
        orderLogMapper.save(orderLog);

    }




}
