package spzxproduct.Service;

import com.atguigu.spzx.model.dto.h5.ProductSkuDto;
import com.atguigu.spzx.model.dto.product.SkuSaleDto;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.atguigu.spzx.model.vo.h5.ProductItemVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/5/3 15:44
 */
public interface ProductService {
    //获取销量数据
    List<ProductSku> findProductSkuBySale();

    //手机端分页查询
    PageInfo<ProductSku> findByPage(Integer page, Integer limit, ProductSkuDto productSkuDto);

    //获取商品详情信息
    ProductItemVo item(Long skuId);
    //远程调用查询product
    ProductSku getBySkuId(Long skuId);

    //远程调用修改支付后的库存量
    Boolean updateSkuSaleNum(List<SkuSaleDto> skuSaleDtoList);
}
