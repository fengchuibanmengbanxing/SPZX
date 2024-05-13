package SPZXCartclient.feign;

import com.atguigu.spzx.model.entity.h5.CartInfo;
import com.atguigu.spzx.model.vo.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/5/6 22:10
 */
@FeignClient(value = "service-cart")
public interface CartFeignClient {

    @GetMapping(value = "/api/order/cart/auth/getAllCkecked")
    public abstract List<CartInfo> getAllCkecked() ;

    //删除已支付订单中的购物车商品
    @GetMapping(value = "api/order/cart/auth/deleteChecked")
    public Result deleteChecked() ;



}