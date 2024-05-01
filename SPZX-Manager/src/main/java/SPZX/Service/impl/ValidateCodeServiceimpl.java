package SPZX.Service.impl;

import SPZX.Service.ValidateCodeService;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.atguigu.spzx.model.vo.system.ValidateCodeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
@Service
public class ValidateCodeServiceimpl implements ValidateCodeService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public ValidateCodeVo generateValidateCode() {

        //生成验证码  hutool工具包生成验证码
        //设置图片宽高数字数量及掩盖线条数量
        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(150, 48, 4, 20);
        String codeValue = circleCaptcha.getCode();
        String imageBase64 = circleCaptcha.getImageBase64();

        //设置键值对的键key使用uuid
        String key= UUID.randomUUID().toString().replace("-","");

        //将验证码写入redis中设置过期时间为5分钟
        stringRedisTemplate.opsForValue().set("user:login:validatecode:"+key,
                codeValue,5, TimeUnit.MINUTES);

        // 构建响应结果数据
        ValidateCodeVo validateCodeVo = new ValidateCodeVo() ;
        validateCodeVo.setCodeKey(key);
        validateCodeVo.setCodeValue("data:image/png;base64," + imageBase64);
        return validateCodeVo;
    }
}
