package SPZXUser.Service.impl;

import SPZXOrder.Utils.HttpUtils;
import SPZXUser.Service.SmsService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author 清醒
 * @Date 2024/5/5 13:33
 */
@Service
public class SmsServiceimpl implements SmsService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //发送验证码
    @Override
    public void sendmesage(String phone) {
        String code=stringRedisTemplate.opsForValue().get(phone);
        if(StringUtils.hasText(code)){
            return;
        }
        //随机生成四位验证码
         code = RandomStringUtils.randomNumeric(4);
        //将验证码存入redis设置过期时间
        stringRedisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);

        //将验证码发送给客户手机号
        sendMesagetoPhone(phone, code);

    }

    private void sendMesagetoPhone(String phone, String code) {

        String host = "https://gyytz.market.alicloudapi.com";
        String path = "/sms/smsSend";
        String method = "POST";
        String appcode = "371997e007fa41039a5e99de7cf108ee";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phone);
        querys.put("param", "**code**:"+code+",**minute**:5");

        querys.put("smsSignId", "2e65b1bb3d054466b82f0c9d125465e2");
        querys.put("templateId", "908e94ccf08b4476ba6c876d13f084ad");
        Map<String, String> bodys = new HashMap<String, String>();


        try {

            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        
    }


}
