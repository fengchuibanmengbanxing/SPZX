package spzxproduct.Mapper;

import com.atguigu.spzx.model.dto.h5.ProductSkuDto;
import com.atguigu.spzx.model.entity.product.ProductSku;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/5/3 15:44
 */
@Mapper

public interface ProductSkuMapper {
    //获取销量数据
    List<ProductSku> findProductSkuBySale();

    //手机端分页查询
    List<ProductSku> findByPage(ProductSkuDto productSkuDto);


    //根据id获取sku对象
    ProductSku getById(Long skuId);
    //同一个商品下面的sku信息列表
    List<ProductSku> findByProductId(Long productId);
    //修改库存
    void updateSale(Long skuId, Integer num);
}
