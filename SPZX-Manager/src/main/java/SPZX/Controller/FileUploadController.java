package SPZX.Controller;

import SPZX.Service.FileUploadService;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import org.apache.ibatis.annotations.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/system")
public class FileUploadController {
    @Autowired
     private FileUploadService fileUploadService;

    @PostMapping(value = "/fileUpload")
    public Result upload(MultipartFile file){
         String url=fileUploadService.upload(file);
        return Result.build(url, ResultCodeEnum.SUCCESS);
    }



}
