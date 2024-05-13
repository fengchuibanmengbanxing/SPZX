package SPZXOrder.Service;

import com.atguigu.spzx.model.entity.system.SysMenu;
import com.atguigu.spzx.model.vo.system.SysMenuVo;

import java.util.List;

public interface SysMenuService {
    //查询所有菜单
    List<SysMenu> findNodes();
    //添加菜单
    void save(SysMenu sysMenu);

    void removeById(Long id);

    List<SysMenuVo> findMenuByUserId();


}
