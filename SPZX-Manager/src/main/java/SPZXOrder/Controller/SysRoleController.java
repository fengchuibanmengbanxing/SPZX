package SPZXOrder.Controller;

import SPZXOrder.Service.SysRoleService;
import com.atguigu.spzx.model.dto.system.SysRoleDto;
import com.atguigu.spzx.model.entity.system.SysRole;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@Slf4j
@RestController
@RequestMapping(value = "/admin/system/sysRole")
public class SysRoleController {
    @Autowired
    SysRoleService sysRoleService;

    @PostMapping("/findByPage/{pageNum}/{pageSize}")
    public Result findByPage(@RequestBody SysRoleDto sysRoleDto,
                             @PathVariable("pageNum") Integer pageNum,
                             @PathVariable("pageSize") Integer pageSize) {
        PageInfo<SysRole> pageInfo = sysRoleService.findByPage(sysRoleDto, pageNum, pageSize);

        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    @PostMapping("/saveSysRole")
    public Result savesysRole(@RequestBody SysRole sysRole) {

        sysRoleService.saveSysRole(sysRole);

        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @PutMapping("/updateSysRole")
    public Result updateSysRole(@RequestBody SysRole sysRole) {

        sysRoleService.updatesysRole(sysRole);

        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @DeleteMapping("/DeleteSysRole/{SysRoleid}")
    public Result DeleteSysRole(@PathVariable("SysRoleid") Long SysRoleid) {

        sysRoleService.DeleteSysRole(SysRoleid);

        return Result.build(null, ResultCodeEnum.SUCCESS);
    }


    // com.atguigu.spzx.manager.controller.SysRoleController
    @GetMapping(value = "/findAllRoles/{userId}")
    public Result<Map<String , Object>> findAllRoles(@PathVariable(value = "userId") Long userId) {
        log.info("================" + userId + "================");
        Map<String, Object> resultMap = sysRoleService.findAllRoles(userId);
        return Result.build(resultMap , ResultCodeEnum.SUCCESS)  ;

    }
}
