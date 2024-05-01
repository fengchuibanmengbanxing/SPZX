package SPZX.Controller;

import SPZX.Service.BrandService;
import com.atguigu.spzx.model.entity.product.Brand;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author 清醒
 * @Date 2024/3/24 16:45
 */

@RestController
@RequestMapping(value = "/admin/product/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("/{page}/{limit}")
    public Result select(@PathVariable("page") int page,
                         @PathVariable("limit") int limit) {
        PageInfo<Brand> PageInfo = brandService.getPageInfo(page, limit);

        return Result.build(PageInfo, ResultCodeEnum.SUCCESS);
    }

    @PostMapping("/save")
    public Result insertinto(@RequestBody Brand brand) {
        brandService.InsertInto(brand);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    //修改品牌
    @PutMapping("updateById")
    public Result updateById(@RequestBody Brand brand) {
        brandService.updateById(brand);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    //删除品牌
    @DeleteMapping("/deleteById/{id}")
    public Result DelectByID(@PathVariable("id") int id){
        brandService.DelectByID(id);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

}
