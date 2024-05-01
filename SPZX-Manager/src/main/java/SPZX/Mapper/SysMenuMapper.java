package SPZX.Mapper;

import com.atguigu.spzx.model.entity.system.SysMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysMenuMapper {
    //查询所有菜单
    List<SysMenu> findall();
    //添加菜单
    void save(SysMenu sysMenu);
    //查询菜单是否存在子菜单
    int selectByid(Long id);
   //根据id删除菜单
    void deleteById(Long id);

    List<SysMenu> selectListByUserId(Long userId);

    SysMenu selectById(Long parentId);
}
