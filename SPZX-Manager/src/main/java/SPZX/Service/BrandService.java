package SPZX.Service;

import com.atguigu.spzx.model.entity.product.Brand;
import com.github.pagehelper.PageInfo;

/**
 * @Author 清醒
 * @Date 2024/3/24 16:46
 */
public interface BrandService {
    //获取分页列表
    PageInfo<Brand> getPageInfo(int  page, int  limit);
    //保存添加
    void InsertInto(Brand brand);
    //修改品牌
    void updateById(Brand brand);
    //删除品牌
    void DelectByID(int id);

}