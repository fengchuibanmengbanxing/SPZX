package spzxproduct.Mapper;

import com.atguigu.spzx.model.entity.product.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author 清醒
 * @Date 2024/5/4 20:31
 */
@Mapper
public interface ProductMapper {
    //根据skuid获取product对象
    Product findById(Long productId);
}
