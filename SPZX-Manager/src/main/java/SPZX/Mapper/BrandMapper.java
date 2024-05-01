package SPZX.Mapper;

import com.atguigu.spzx.model.entity.product.Brand;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/3/24 16:46
 */
@Mapper
public interface BrandMapper {
    //获取分页对象集合
    List<Brand> getPageInfo();
    //保存添加
    void InsertInto(Brand brand);
    //修改品牌
    void updateById(Brand brand);
    //删除品牌 逻辑删除
    void DelectByID(int id);
}
