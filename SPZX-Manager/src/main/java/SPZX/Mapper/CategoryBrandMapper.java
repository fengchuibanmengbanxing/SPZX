package SPZX.Mapper;

import com.atguigu.spzx.model.dto.product.CategoryBrandDto;
import com.atguigu.spzx.model.entity.product.Brand;
import com.atguigu.spzx.model.entity.product.CategoryBrand;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/5/1 11:22
 */
@Mapper
public interface CategoryBrandMapper {

    //查找品牌
    public abstract List<CategoryBrand> findPage(CategoryBrandDto categoryBrandDto);

    public abstract void save(CategoryBrand categoryBrand);

    List<Brand> findBrandByCategoryId(Long categoryId);


}
