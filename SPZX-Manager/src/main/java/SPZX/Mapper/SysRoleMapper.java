package SPZX.Mapper;

import com.atguigu.spzx.model.dto.system.SysRoleDto;
import com.atguigu.spzx.model.entity.system.SysRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface SysRoleMapper {
    List<SysRole> findByPage(SysRoleDto sysRoleDto);

    void saveSysRole(SysRole sysRole);

    void updatesysRole(SysRole sysRole);

    void DeleteSysRole(Long sysRoleid);

    //查询所有角色列表
    List<SysRole> findall();

}
