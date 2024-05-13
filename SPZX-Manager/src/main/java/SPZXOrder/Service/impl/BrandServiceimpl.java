package SPZXOrder.Service.impl;

import SPZXOrder.Mapper.BrandMapper;
import SPZXOrder.Service.BrandService;
import com.atguigu.spzx.model.entity.product.Brand;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/3/24 16:46
 */
@Service
public class BrandServiceimpl implements BrandService {
    @Autowired
    private BrandMapper brandMapper;
    @Override
    public PageInfo<Brand> getPageInfo(int page, int limit) {
        //设置起始页
        PageHelper.startPage(page,limit);
        //获取分页对象集合
        List<Brand> list=brandMapper.getPageInfo();
        //创建分页列表
        PageInfo<Brand> PageInfo = new PageInfo<>(list);
        return PageInfo;
    }
    //添加品牌
    @Override
    public void InsertInto(Brand brand) {
        brandMapper.InsertInto(brand);
    }
    //修改品牌
    @Override
    public void updateById(Brand brand) {
        brandMapper.updateById(brand);
    }
    //删除品牌
    @Override
    public void DelectByID(int id) {
        brandMapper.DelectByID(id);
    }

    @Override
    public List<Brand> findAll() {
        List<Brand> Brandall = brandMapper.findAll();
        return  Brandall;

    }


}
