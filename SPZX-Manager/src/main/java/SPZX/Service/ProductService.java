package SPZX.Service;

import com.atguigu.spzx.model.dto.product.ProductDto;
import com.atguigu.spzx.model.entity.product.Product;
import com.github.pagehelper.PageInfo;

/**
 * @Author 清醒
 * @Date 2024/5/1 19:27
 */
public interface ProductService {
    PageInfo<Product> findByPage(Integer page, Integer limit, ProductDto productDto);

    void save(Product product);

    //根据商品id查询商品信息
    Product ProductfindById(Long id);

    //保存修改后数据
    void updateById(Product product);
    //删除数据
    void deleteById(Long id);
    //审核状态
    void updateAuditStatus(Long id, Integer auditStatus);
    //上下架
    void updateStatus(Long id, Integer status);

}
