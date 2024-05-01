package SPZX.Controller;

import SPZX.Service.SysMenuRoleService;
import SPZX.Service.SysMenuService;
import com.atguigu.spzx.model.dto.system.AssginMenuDto;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author 清醒
 * @Date 2024/3/18 20:13
 */
@RestController
@RequestMapping("/admin/system/sysRoleMenu")
public class SysMenuRoleController {

    @Autowired
    private SysMenuRoleService sysMenuRoleService;

    @GetMapping(value = "/findSysRoleMenuByRoleId/{roleId}")
    public Result findSysRoleMenuByRoleId (@PathVariable("roleId") Long roleId){
        //根据角色id查找对应数据库中已经分配的菜单集合
        Map<String,Object> map= sysMenuRoleService.findSysRoleMenuByRoleId(roleId);

        return Result.build(map, ResultCodeEnum.SUCCESS);
    }
    //保存菜单
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginMenuDto assginMenuDto) {
        sysMenuRoleService.doAssign(assginMenuDto);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }


}
