package spzxproduct.Service;

import com.atguigu.spzx.model.entity.product.Category;

import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/5/3 15:43
 */
public interface CategoryService {
    //获取一级分类
    List<Category> findOneCategory();

    //获取分类树形结构
    List<Category> findCategoryTree();

}
