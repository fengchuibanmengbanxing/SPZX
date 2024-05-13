package SPZXOrder.Mapper;

import com.atguigu.spzx.model.entity.product.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/3/23 21:48
 */
@Mapper
public interface CategoryMapper {

    List<Category> CategoryById(Long id);

    int CountcategoryById(Long id);
    //获取数据库category集合信息
    List<Category> findallcategory();


    void batchInsert(List cachedDataList);
}
