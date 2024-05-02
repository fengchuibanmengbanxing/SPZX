package SPZX.Service.impl;

import SPZX.Mapper.CategoryBrandMapper;
import SPZX.Service.CategoryBrandService;
import com.atguigu.spzx.model.dto.product.CategoryBrandDto;
import com.atguigu.spzx.model.entity.product.Brand;
import com.atguigu.spzx.model.entity.product.CategoryBrand;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/5/1 15:36
 */

@Service
public class CategoryBrandServiceimpl implements CategoryBrandService {
    @Autowired
    private CategoryBrandMapper categoryBrandMapper;


    @Override
    public PageInfo<CategoryBrand> findPage(int page, int limit, CategoryBrandDto categoryBrandDto) {
        PageHelper.startPage(page, limit);
        List<CategoryBrand> categoryBrandList = categoryBrandMapper.findPage(categoryBrandDto) ;
        return new PageInfo<>(categoryBrandList);
    }

    @Override
    public void save(CategoryBrand categoryBrand) {
        categoryBrandMapper.save(categoryBrand) ;
    }


    //根据类别查询品牌信息
    @Override
    public List<Brand> findBrandByCategoryId(Long categoryId) {
        List<Brand> list=categoryBrandMapper.findBrandByCategoryId(categoryId);
        return list;
    }
}
