package SPZXOrder.Mapper;

import com.atguigu.spzx.model.entity.product.ProductDetails;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author 清醒
 * @Date 2024/5/1 21:28
 */
@Mapper
public interface ProductDetailsMapper {
    void save(ProductDetails productDetails);

    //查询商品详情信息
    ProductDetails findProductDetailsById(Long productId);


    // 修改商品的详情数据

    void updateById(ProductDetails productDetails);

    void deleteByProductId(Long id);
}
