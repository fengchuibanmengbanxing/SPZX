package SPZXOrder.Service.impl;

import SPZXOrder.Exception.GuiguException;
import SPZXOrder.Listener.ExcelListener;
import SPZXOrder.Mapper.CategoryMapper;
import SPZXOrder.Service.CategoryService;
import com.alibaba.excel.EasyExcel;
import com.atguigu.spzx.model.entity.product.Category;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.product.CategoryExcelVo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/3/19 18:57
 */

@Service
public class CategoryServiceimpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> fingcategoryById(Long id) {
        //查询所有父节点为id的子节点返回
        List<Category> list = categoryMapper.CategoryById(id);
        if (!CollectionUtils.isEmpty(list)) {
            //遍历所有节点同时检查节点是否存在子节点设置has
            list.forEach(Category -> {
                        int cout = categoryMapper.CountcategoryById(id);
                        if (cout > 0) {
                            Category.setHasChildren(true);
                        } else {
                            Category.setHasChildren(false);
                        }
                    }
            );
        }
        return list;
    }

    @Override
    public void exportData(HttpServletResponse response) {
        try {
            // 设置响应结果类型
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            //设置字符编码
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("分类数据", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            //获取数据库category集合信息
            List<Category> Categorylist = categoryMapper.findallcategory();
            ArrayList<Object> CategoryVolist = new ArrayList<>();
            //转化categoty成categoryvo集合
            Categorylist.forEach(Category -> {
                CategoryExcelVo categoryExcelVo = new CategoryExcelVo();
                BeanUtils.copyProperties(Category, categoryExcelVo, CategoryExcelVo.class);
                //将转换对象存入集合
                CategoryVolist.add(categoryExcelVo);
            });


//            // 将从数据库中查询到的Category对象转换成CategoryExcelVo对象
//            for(Category category : Categorylist) {
//                CategoryExcelVo categoryExcelVo = new CategoryExcelVo();
//                BeanUtils.copyProperties(category, categoryExcelVo, CategoryExcelVo.class);
//                CategoryVolist.add(categoryExcelVo);
//            }

            //将categoryvo集合数据写入excel文件中
            // 写出数据到浏览器端
            EasyExcel.write(response.getOutputStream(), CategoryExcelVo.class).sheet("分类数据").doWrite(CategoryVolist);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }

    }

    //导入
    @Override
    public void importData(MultipartFile file) {
        try {
            //创建监听器对象，传递mapper对象
            ExcelListener<CategoryExcelVo> excelListener = new ExcelListener<>(categoryMapper);
            //调用read方法读取excel数据
            EasyExcel.read(file.getInputStream(),
                    CategoryExcelVo.class,
                    excelListener).sheet().doRead();
        } catch (IOException e) {
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }
    }


}
