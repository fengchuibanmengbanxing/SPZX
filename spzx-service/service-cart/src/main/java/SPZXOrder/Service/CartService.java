package SPZXOrder.Service;

import com.atguigu.spzx.model.entity.h5.CartInfo;

import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/5/6 13:54
 */
public interface CartService {
    //添加商品数量
    void addToCart(Long skuId, Integer skuNum);
    //提取购物车列表
    List<CartInfo> getCartList();
    //删除购物车
    void deleteCart(Long skuId);
    //更新选中状态
    void checkCart(Long skuId, Integer isChecked);
    //更新全选
    void allCheckCart(Integer isChecked);
    //清空购物车
    void clearCart();
    //获取购物车选中商品
    List<CartInfo> getAllCkecked();
     //删除已支付订单中的购物车商品
    void deleteChecked();

}
