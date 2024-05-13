package spzxproduct.Mapper;

import com.atguigu.spzx.model.entity.product.ProductDetails;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author 清醒
 * @Date 2024/5/4 20:30
 */
@Mapper
public interface ProductDetailsMapper {
    //商品详情信息
    ProductDetails getByProductId(Long productId);
}
