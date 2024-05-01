package SPZX.Service;

import com.atguigu.spzx.model.dto.system.AssginMenuDto;

import java.util.Map;

/**
 * @Author 清醒
 * @Date 2024/3/18 20:13
 */
public interface SysMenuRoleService {
    Map<String, Object> findSysRoleMenuByRoleId(Long roleId);

    void doAssign(AssginMenuDto assginMenuDto);
}
