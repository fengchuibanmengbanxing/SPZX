package spzx;

import com.alibaba.excel.EasyExcel;
import com.atguigu.spzx.model.vo.product.CategoryExcelVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/3/24 10:26
 */
public class EasyExcelTest {
    public static void main(String[] args) {
        readDateToExcel();
//        writeDataToExcel();
    }

    //读取方法
    public static void readDateToExcel() {
        String fileName = "D://easyexcl//分类数据.xlsx";
        // 创建一个监听器对象
        ExcelListener<CategoryExcelVo> excelListener = new ExcelListener<>();
        EasyExcel.read(fileName, CategoryExcelVo.class, excelListener).sheet().doRead();         // 解析excel表格
        List<CategoryExcelVo> excelVoList = excelListener.getDatas();    //获取解析到的数据
        excelVoList.forEach(s -> System.out.println(s));   // 进行遍历操作

    }



    public static void writeDataToExcel() {
        List<CategoryExcelVo> list = new ArrayList<>() ;
        list.add(new CategoryExcelVo(1L , "数码办公" , "",0L, 1, 1)) ;
        list.add(new CategoryExcelVo(11L , "华为手机" , "",1L, 1, 2)) ;
        EasyExcel.write("D://easyexcl//分类数据.xlsx" , CategoryExcelVo.class).sheet("分类数据1").doWrite(list);
    }
}
