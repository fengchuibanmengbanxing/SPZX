package SPZXOrder.Service.impl;

import SPZXOrder.AuthContextUtil;
import SPZXOrder.Exception.GuiguException;
import SPZXOrder.Mapper.SysMenuMapper;
import SPZXOrder.Mapper.SysMenuRoleMapper;
import SPZXOrder.Service.SysMenuService;
import SPZXOrder.utiles.MenuHelper;
import com.atguigu.spzx.model.entity.system.SysMenu;

import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.system.SysMenuVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class SysMenuServiceimpl implements SysMenuService {
    @Autowired
    SysMenuRoleMapper sysMenuRoleMapper;
    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> findNodes() {
        //查询所有数据
        List<SysMenu> menulist=sysMenuMapper.findall();
        //空集合返回空
        if(CollectionUtils.isEmpty(menulist)){
            return null;
        }

        //将数据转化为树形结构返回
        List<SysMenu> treelist=MenuHelper.buildTree(menulist);
        return treelist;

    }
    @Transactional
    //添加菜单
    @Override
    public void save(SysMenu sysMenu) {

        sysMenuMapper.save(sysMenu);

        // 新添加一个菜单，那么此时就需要将该菜单所对应的父级菜单设置为半开
        updateSysRoleMenuIsHalf(sysMenu) ;

    }

    private void updateSysRoleMenuIsHalf(SysMenu sysMenu) {

        // 查询是否存在父节点
        SysMenu parentMenu = sysMenuMapper.selectById(sysMenu.getParentId());
        //存在父节点
        if (parentMenu != null) {
            // 将该id的菜单设置为半开
           sysMenuRoleMapper.updateSysRoleMenuIsHalf(parentMenu.getId());
            // 递归调用
            updateSysRoleMenuIsHalf(parentMenu);

        }
    }

    @Override
    public void removeById(Long id) {
        //判断当前菜单是否存在子菜单
        int count=sysMenuMapper.selectByid(id);
        //存在则不允许删除
        if(count>0){
            throw new GuiguException(ResultCodeEnum.NODE_ERROR);
        }
        //不存在则进行删除
        sysMenuMapper.deleteById(id);
    }

    @Override
    public List<SysMenuVo> findMenuByUserId() {
        //查询登录用户Id
        Long UserId = AuthContextUtil.get().getId();
        //以用户id为条件查找符合菜单
        List<SysMenu> sysMenuList=sysMenuMapper.selectListByUserId(UserId);
        //将菜单封装为SysMenuVo对象返回前端
        //构建树形数据
        List<SysMenu> sysMenuTreeList = MenuHelper.buildTree(sysMenuList);
        return this.buildMenus(sysMenuTreeList);
    }

    // 将List<SysMenu>对象转换成List<SysMenuVo>对象
    private List<SysMenuVo> buildMenus(List<SysMenu> menus) {

        List<SysMenuVo> sysMenuVoList = new LinkedList<SysMenuVo>();
        for (SysMenu sysMenu : menus) {
            SysMenuVo sysMenuVo = new SysMenuVo();
            sysMenuVo.setTitle(sysMenu.getTitle());
            sysMenuVo.setName(sysMenu.getComponent());
            List<SysMenu> children = sysMenu.getChildren();
            if (!CollectionUtils.isEmpty(children)) {
                sysMenuVo.setChildren(buildMenus(children));
            }
            sysMenuVoList.add(sysMenuVo);
        }
        return sysMenuVoList;
    }

}
