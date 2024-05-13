package SPZXOrder.Service.impl;

import SPZXOrder.Mapper.SysMenuMapper;
import SPZXOrder.Mapper.SysMenuRoleMapper;
import SPZXOrder.Service.SysMenuRoleService;
import com.atguigu.spzx.model.dto.system.AssginMenuDto;
import com.atguigu.spzx.model.entity.system.SysMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 清醒
 * @Date 2024/3/18 20:14
 */
@Service
public class SysMenuRoleServiceimpl implements SysMenuRoleService {
    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private SysMenuRoleMapper sysMenuRoleMapper;

    @Override
    public Map<String, Object> findSysRoleMenuByRoleId(Long roleId) {
        //查询所有菜单集合
        List<SysMenu> sysMenuList = sysMenuMapper.findall();
        //根据角色id查找角色菜单关联表返回已经分配的菜单id集合
        List<Long> roleMenuIds = sysMenuRoleMapper.findSysRoleMenuByRoleId(roleId);
        Map<String, Object> result = new HashMap<>();
        //所有角色集合
        result.put("sysMenuList", sysMenuList);
        //角色已经分配菜单集合
        result.put("roleMenuIds", roleMenuIds);
        return result;
    }

    @Override
    public void doAssign(AssginMenuDto assginMenuDto) {
        //删除assginMenuDto角色id原有的分配菜单
        sysMenuRoleMapper.deleteByRoleId(assginMenuDto.getRoleId());
        //将前端传入的菜单集合写入数据库
        List<Map<String, Number>> menuInfo = assginMenuDto.getMenuIdList();
        if(menuInfo != null && menuInfo.size() > 0) {
            sysMenuRoleMapper.doAssign(assginMenuDto) ;
        }
    }
}
