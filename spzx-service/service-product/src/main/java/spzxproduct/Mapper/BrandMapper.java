package spzxproduct.Mapper;

import com.atguigu.spzx.model.entity.product.Brand;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/5/4 19:25
 */
@Mapper
public interface BrandMapper {
    //查询所有品牌
    List<Brand> findAll();

}
