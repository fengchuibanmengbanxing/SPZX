package SPZX.Controller;

import SPZX.AuthContextUtil;
import SPZX.Service.SysMenuService;
import SPZX.Service.SysUserService;
import SPZX.Service.ValidateCodeService;
import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.entity.system.SysMenu;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.system.LoginVo;
import com.atguigu.spzx.model.vo.system.SysMenuVo;
import com.atguigu.spzx.model.vo.system.ValidateCodeVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户接口")//swg中给类的注解
@RestController
@RequestMapping(value = "/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService ;
    @Autowired
    ValidateCodeService validateCodeService;

    @Autowired
    SysMenuService sysMenuService;

    @Operation(summary = "登录接口")//swg中给类中方法的注解
    @PostMapping(value = "/login")
    public Result<LoginVo> login(@RequestBody LoginDto loginDto) {
        LoginVo loginVo = sysUserService.login(loginDto) ;
//        ResultCodeEnum  枚举类存放固定内容
        return Result.build(loginVo , ResultCodeEnum.SUCCESS) ;
    }

    @Operation(summary = "验证码接口")//swg中给类中方法的注解
    @GetMapping(value = "/generateValidateCode")
    public Result<ValidateCodeVo> generateValidateCode() {
        ValidateCodeVo validateCodeVo = validateCodeService.generateValidateCode();
        return Result.build(validateCodeVo , ResultCodeEnum.SUCCESS) ;
    }



    @GetMapping(value = "/getUserInfo")
    public Result<SysUser> loginInfo(@RequestHeader(name = "token") String token) {
        //读取存储在Threadlocal中的对象数据
        return Result.build(AuthContextUtil.get(), ResultCodeEnum.SUCCESS) ;
    }



    @GetMapping(value = "/logout")
    public Result logout(@RequestHeader(value = "token") String token) {
     sysUserService.logout(token);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }


    //动态获取菜单
    @GetMapping("/menus")
    public Result getmenus() {
        List<SysMenuVo> MenuByUserIdlist = sysMenuService.findMenuByUserId();
        return Result.build(MenuByUserIdlist, ResultCodeEnum.SUCCESS);
    }
}
