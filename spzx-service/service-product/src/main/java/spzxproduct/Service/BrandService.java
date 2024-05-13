package spzxproduct.Service;

import com.atguigu.spzx.model.entity.product.Brand;

import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/5/4 19:24
 */

public interface BrandService {
    //查询所有品牌
    List<Brand> findAll();

}
