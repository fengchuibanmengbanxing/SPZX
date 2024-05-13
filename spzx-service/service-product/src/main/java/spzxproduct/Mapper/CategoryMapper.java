package spzxproduct.Mapper;

import com.atguigu.spzx.model.entity.product.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/5/3 15:44
 */
@Mapper
public interface CategoryMapper {
    //获取一级分类
    List<Category> findOneCategory();

    //获取所有分类
    List<Category> findAll();

}
