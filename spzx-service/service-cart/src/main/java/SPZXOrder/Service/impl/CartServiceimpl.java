package SPZXOrder.Service.impl;

import SPZXOrder.Service.CartService;
import SPZXOrder.Util.AuthContextUtil;
import SPZXOrder.feign.ProductFeignClient;
import com.alibaba.fastjson.JSON;
import com.atguigu.spzx.model.entity.h5.CartInfo;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.atguigu.spzx.model.entity.user.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 清醒
 * @Date 2024/5/6 13:54
 */
@Service
@Slf4j
public class CartServiceimpl implements CartService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ProductFeignClient productFeignClient;

    //添加商品数量
    @Override
    public void addToCart(Long skuId, Integer skuNum) {
        //校验用户登录状态从中获取用户id设置购物车
        // threadlocal获取
        UserInfo userInfo = AuthContextUtil.getUserInfo();
        Long userId = userInfo.getId();
        //根据id查询reids购物车中是否存在该商品
        Object cartInfoObj = stringRedisTemplate.opsForHash()
                .get("User:Cart:" + userId, String.valueOf(skuId));
        CartInfo cartInfo = new CartInfo();
        //存在则将数量相加
        if (cartInfoObj != null) {
            //存在
            cartInfo = JSON.parseObject(cartInfoObj.toString(), CartInfo.class);
            cartInfo.setSkuNum(cartInfo.getSkuNum() + skuNum);
            //设置购物车商品为选中状态
            cartInfo.setIsChecked(1);
            //修改时间
            cartInfo.setUpdateTime(new Date());
        } else {
            cartInfo = new CartInfo();
            ProductSku productSku = productFeignClient.getBySkuId(skuId);
//            .getData()
            cartInfo.setCartPrice(productSku.getSalePrice());
            cartInfo.setSkuNum(skuNum);
            cartInfo.setSkuId(skuId);
            cartInfo.setUserId(userId);
            cartInfo.setImgUrl(productSku.getThumbImg());
            cartInfo.setSkuName(productSku.getSkuName());
            cartInfo.setIsChecked(1);
            cartInfo.setCreateTime(new Date());
            cartInfo.setUpdateTime(new Date());
            //不存在则将商品加入购物车
            stringRedisTemplate.opsForHash().put("User:Cart:" + userId, String.valueOf(skuId), JSON.toJSONString(cartInfo));

        }
    }

    @Override
    public List<CartInfo> getCartList() {

        //根据Treadlocal获取登录对象id
        Long userId = AuthContextUtil.getUserInfo().getId();
        //根据userid查询reids获取购物车列表
        List<Object> CartInfoList = stringRedisTemplate.opsForHash().values("User:Cart:" + userId);
        if (!CollectionUtils.isEmpty(CartInfoList)) {
            //将获取list集合转化为List<CartInfo>
            List<CartInfo> collect = CartInfoList.stream().map(cartinfo ->
                            JSON.parseObject(cartinfo.toString(), CartInfo.class)).
                    sorted((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime())).
                    collect(Collectors.toList());
            return collect;
        }
        //返回空集合
        return new ArrayList<>();
    }

    //删除购物车
    @Override
    public void deleteCart(Long skuId) {
        //根据Treadlocal获取登录对象id
        Long userId = AuthContextUtil.getUserInfo().getId();
        stringRedisTemplate.opsForHash().delete("User:Cart:" + userId, skuId.toString());
    }

    //更新选中状态
    @Override
    public void checkCart(Long skuId, Integer isChecked) {
        //获取用户id
        UserInfo userInfo = AuthContextUtil.getUserInfo();
        Long userId = userInfo.getId();
        //根据userId以及skuId查询redis中是否有该商品
        Object cartInfoobj = stringRedisTemplate.opsForHash().get("User:Cart:" + userId, skuId.toString());
        //取出商品cartInfo设置选中状态
        CartInfo cartInfo = JSON.parseObject(cartInfoobj.toString(), CartInfo.class);
        cartInfo.setIsChecked(isChecked);
        //将cartInfo写入redis
        stringRedisTemplate.opsForHash().put("User:Cart:" + userId, String.valueOf(skuId), JSON.toJSONString(cartInfo));

    }

    //更新购物车状态
    @Override
    public void allCheckCart(Integer isChecked) {
        //获取用户id
        UserInfo userInfo = AuthContextUtil.getUserInfo();
        Long userId = userInfo.getId();
        //获取用户所有购物车信息
        List<Object> cartInfoObjList = stringRedisTemplate.opsForHash().values("User:Cart:" + userId);
        //将所有购物车信息选中状态更新
        if (!CollectionUtils.isEmpty(cartInfoObjList)) {
            List<CartInfo> cartInfoList = cartInfoObjList.stream().map(cartInfoObj -> JSON.parseObject(cartInfoObj.toString(), CartInfo.class))
                    .collect(Collectors.toList());
            //设置所有商品选中状态
            cartInfoList.stream().forEach(cartInfo -> {
                cartInfo.setIsChecked(isChecked);
                //将更新后的数据写回redis
                stringRedisTemplate.opsForHash().put("User:Cart:" + userId, cartInfo.getSkuId().toString(), JSON.toJSONString(cartInfo));
            });

        }


    }

    //清空购物车
    @Override
    public void clearCart() {
        //获取用户id
        UserInfo userInfo = AuthContextUtil.getUserInfo();
        Long userId = userInfo.getId();
        stringRedisTemplate.delete("User:Cart:" + userId);
    }

    @Override
    public List<CartInfo> getAllCkecked() {
        //获取用户id
        Long userId =AuthContextUtil.getUserInfo().getId();
        List<Object> cartInfoObjList = stringRedisTemplate.opsForHash().values("User:Cart:" + userId);
        boolean empty = cartInfoObjList.isEmpty();
        log.info(empty+" ");
        //获取选中商品
        if (!CollectionUtils.isEmpty(cartInfoObjList)) {
            List<CartInfo> cartInfoList = cartInfoObjList.stream().map(cartInfo ->
                    JSON.parseObject(cartInfo.toString(), CartInfo.class)).filter(cartInfo -> cartInfo.getIsChecked()==1).collect(Collectors.toList());
            return cartInfoList;
        }
        return null;
    }

    //删除已支付订单中的购物车商品
    @Override
    public void deleteChecked() {
        //获取用户id
        Long userId =AuthContextUtil.getUserInfo().getId();
        List<Object> cartInfoObjList = stringRedisTemplate.opsForHash().values("User:Cart:" + userId);
        //删除已购买商品
        if(!CollectionUtils.isEmpty(cartInfoObjList)){
            cartInfoObjList.stream().map(cartInfoObj->JSON.parseObject(cartInfoObj.toString(),CartInfo.class))
                    .filter(cartInfo -> cartInfo.getIsChecked()==1)
                    .forEach(cartInfo->stringRedisTemplate.opsForHash().delete("User:Cart:"+userId,String.valueOf(cartInfo.getSkuId())));
        }

    }

}
