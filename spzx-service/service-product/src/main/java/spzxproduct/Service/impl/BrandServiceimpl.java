package spzxproduct.Service.impl;

import com.atguigu.spzx.model.entity.product.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spzxproduct.Mapper.BrandMapper;
import spzxproduct.Service.BrandService;

import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/5/4 19:24
 */
@Service
public class BrandServiceimpl implements BrandService {
    @Autowired
    private BrandMapper brandMapper;
    //查询所有品牌
    @Override
    public List<Brand> findAll() {
        List<Brand> brandList=brandMapper.findAll();
        return brandList;
    }
}
