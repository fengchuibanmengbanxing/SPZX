package SPZXOrder.Mapper;

import com.atguigu.spzx.model.entity.base.ProductUnit;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/5/1 21:01
 */
@Mapper
public interface ProductUnitMapper {


    List<ProductUnit> findAll();

}
