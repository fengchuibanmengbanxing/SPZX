package SPZXOrder.Service.impl;

import SPZXOrder.Mapper.ProductUnitMapper;
import SPZXOrder.Service.ProductUnitService;
import com.atguigu.spzx.model.entity.base.ProductUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/5/1 21:00
 */
@Service
public class ProductUnitServiceimpl  implements ProductUnitService {


    @Autowired
    private ProductUnitMapper productUnitMapper;

    @Override
    public List<ProductUnit> findAll() {
        return productUnitMapper.findAll() ;
    }
}
