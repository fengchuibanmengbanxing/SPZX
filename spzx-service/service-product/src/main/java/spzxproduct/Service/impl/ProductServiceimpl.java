package spzxproduct.Service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.atguigu.spzx.model.dto.h5.ProductSkuDto;
import com.atguigu.spzx.model.dto.product.SkuSaleDto;
import com.atguigu.spzx.model.entity.product.Product;
import com.atguigu.spzx.model.entity.product.ProductDetails;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.atguigu.spzx.model.vo.h5.ProductItemVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spzxproduct.Mapper.ProductDetailsMapper;
import spzxproduct.Mapper.ProductMapper;
import spzxproduct.Mapper.ProductSkuMapper;
import spzxproduct.Service.ProductService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 清醒
 * @Date 2024/5/3 15:44
 */
@Service
public class ProductServiceimpl implements ProductService {

    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private ProductDetailsMapper productDetailsMapper;
    @Autowired
    private ProductMapper productMapper;

    //获取销量数据
    @Override
    public List<ProductSku> findProductSkuBySale() {
        return productSkuMapper.findProductSkuBySale();
    }

    //手机端分页查询
    @Override
    public PageInfo<ProductSku> findByPage(Integer page, Integer limit, ProductSkuDto productSkuDto) {
        PageHelper.startPage(page, limit);
        List<ProductSku> productSkuList = productSkuMapper.findByPage(productSkuDto);
        PageInfo<ProductSku> pageInfo = new PageInfo<>(productSkuList);
        return pageInfo;
    }

    @Override
    public ProductItemVo item(Long skuId) {
        //根据skuid获取productsku信息
        ProductSku productSku = productSkuMapper.getById(skuId);
        //根据sku对象id获取product对象
        Long productId = productSku.getProductId();
        //根据id获取product对象
        Product product = productMapper.findById(productId);
        //商品详情信息
        ProductDetails productDetails = productDetailsMapper.getByProductId(productSku.getProductId());
        //同一个商品下面的sku信息列表
        List<ProductSku> productSkuList = productSkuMapper.findByProductId(productSku.getProductId());
        //建立sku规格与skuId对应关系
        Map<String, Object> skuSpecValueMap = new HashMap<>();
        productSkuList.forEach(item -> {
            skuSpecValueMap.put(item.getSkuSpec(), item.getId());
        });

        ProductItemVo productItemVo = new ProductItemVo();
        productItemVo.setProduct(product);
        productItemVo.setProductSku(productSku);
        productItemVo.setSkuSpecValueMap(skuSpecValueMap);
        //封装详情图片
        String imageUrls = productDetails.getImageUrls();
        String[] split = imageUrls.split(",");
        List<String> strings = Arrays.asList(split);
        productItemVo.setDetailsImageUrlList(strings);
        //封装轮播图图片
        String sliderUrls = product.getSliderUrls();
        String[] split1 = sliderUrls.split(",");
        List<String> strings1 = Arrays.asList(split1);
        productItemVo.setSliderUrlList(strings1);

        String specValue = product.getSpecValue();
        JSONArray jsonArray = JSON.parseArray(specValue);
        productItemVo.setSpecValueList(jsonArray);
        return productItemVo;
    }

    @Override
    public ProductSku getBySkuId(Long skuId) {

        return productSkuMapper.getById(skuId);
    }


    @Transactional
    @Override
    public Boolean updateSkuSaleNum(List<SkuSaleDto> skuSaleDtoList) {
        if(!CollectionUtils.isEmpty(skuSaleDtoList)) {
            for(SkuSaleDto skuSaleDto : skuSaleDtoList) {
                productSkuMapper.updateSale(skuSaleDto.getSkuId(), skuSaleDto.getNum());
            }
        }
        return true;
    }


}
