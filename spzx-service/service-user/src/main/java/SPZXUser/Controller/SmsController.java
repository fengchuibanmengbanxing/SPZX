package SPZXUser.Controller;

import SPZXUser.Service.SmsService;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 清醒
 * @Date 2024/5/5 13:31
 */
@RestController
@RequestMapping("api/user/sms")
public class SmsController {
    @Autowired
    public SmsService smsService;
    @GetMapping(value = "/sendCode/{phone}")
    public Result sendmesage(@PathVariable String phone) {
        smsService.sendmesage(phone);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

}
