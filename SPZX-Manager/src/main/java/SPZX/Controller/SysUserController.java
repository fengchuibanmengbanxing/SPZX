package SPZX.Controller;

import SPZX.Service.SysUserService;
import com.atguigu.spzx.model.dto.system.AssginRoleDto;
import com.atguigu.spzx.model.dto.system.SysUserDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/admin/system/sysUser")
public class SysUserController {
    @Autowired
    SysUserService sysUserService;

    //用户分页查询接口
    @GetMapping(value = "/findByPage/{pageNum}/{pageSize}")
    public Result findPage(@PathVariable("pageNum") Integer pageNum,
                             @PathVariable("pageSize") Integer pageSize,
                             SysUserDto sysUserDto){
        PageInfo<SysUser> PageInfo =sysUserService.findPage(pageNum,pageSize,sysUserDto);
        return Result.build(PageInfo, ResultCodeEnum.SUCCESS);

    }


    //用户添加
    @PostMapping(value = "/saveSysUser")
    public Result save(@RequestBody SysUser sysUser){
        sysUserService.save(sysUser);
        return Result.build(null, ResultCodeEnum.SUCCESS);

    }

    //用户修改
    @PutMapping(value = "/updateSysUser")
    public Result update(@RequestBody SysUser sysUser){
        sysUserService.update(sysUser);
        return Result.build(null, ResultCodeEnum.SUCCESS);

    }

    //用户删除
    @DeleteMapping (value = "/deleteById/{userId}")
    public Result delete(@PathVariable("userId") Integer userId){
        sysUserService.delete(userId);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }


    //分配角色
    @PostMapping(value = "/doAssign")
    public Result doAssgin(@RequestBody AssginRoleDto assginRoleDto){
        sysUserService.doAssgin(assginRoleDto);
        return Result.build(null, ResultCodeEnum.SUCCESS);

    }


}
