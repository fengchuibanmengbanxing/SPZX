package SPZX.Controller;

import SPZX.Service.CategoryService;
import com.atguigu.spzx.model.entity.product.Category;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/3/19 18:57
 */

@RestController
@RequestMapping(value = "/admin/product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "根据parentId获取下级节点")
    @GetMapping(value = "/findByParentId/{parentId}")
    public Result Findcategory(@PathVariable Long parentId) {
        //返回所有父节点为parentId的子节点
        List<Category> Categorylist = categoryService.fingcategoryById(parentId);

        return Result.build(Categorylist, ResultCodeEnum.SUCCESS);
    }

    @GetMapping(value = "/exportData")
    public void exportData(HttpServletResponse response) {

        categoryService.exportData(response);

    }


    //MultipartFile是SpringMVC提供简化上传操作的工具类。
    @PostMapping("importData")
    public Result importData(MultipartFile file) {
        //ExcelListenner

        categoryService.importData(file);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

}
