package SPZX.Mapper;

import com.atguigu.spzx.model.entity.product.ProductSku;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/5/1 21:28
 */
@Mapper
public interface ProductSkuMapper {
    void save(ProductSku productSku);

    //查询商品sku信息
    List<ProductSku> findProductSkuById(Long productId);
    // 修改商品的sku数据
    void updateById(ProductSku productSku);

    void deleteByProductId(Long id);
}
