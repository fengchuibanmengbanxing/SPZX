package SPZX.Service;

import com.atguigu.spzx.model.dto.product.CategoryBrandDto;
import com.atguigu.spzx.model.entity.product.Brand;
import com.atguigu.spzx.model.entity.product.CategoryBrand;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/5/1 11:21
 */
public interface CategoryBrandService {
    //查找品牌
    PageInfo<CategoryBrand> findPage(int page, int limit, CategoryBrandDto categoryBrandDto);



     void save(CategoryBrand categoryBrand) ;


    //根据类别查询品牌信息
    List<Brand> findBrandByCategoryId(Long categoryId);

}
