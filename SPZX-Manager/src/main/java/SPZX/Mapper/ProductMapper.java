package SPZX.Mapper;

import com.atguigu.spzx.model.dto.product.ProductDto;
import com.atguigu.spzx.model.entity.product.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/5/1 19:28
 */
@Mapper
public interface ProductMapper {


    // 保存商品数据
    void save(Product product);

    //分页数据
    List<Product> findByPage(ProductDto productDto);

    //查询商品基本信息byid
    Product findProductById(Long id);

    // 修改商品基本数据
    void updateById(Product product);

    void deleteById(Long id);

}
