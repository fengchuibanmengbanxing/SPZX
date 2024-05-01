package SPZX.utiles;

import com.atguigu.spzx.model.entity.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 清醒
 * @Date 2024/3/18 16:43
 */
public class MenuHelper {
//
//    public static List<SysMenu>  buildTree(List<SysMenu> menulist){
//       //从已有的menulist集合中找到一号节点
//        List<SysMenu> trees = new ArrayList<>();
//        for(SysMenu sysMenu: menulist){
//            if(sysMenu.getParentId()==0){
//                trees.add(findChildren(sysMenu,menulist));
//            }
//        }
//        return trees;
//    }
//
//    private static SysMenu findChildren(SysMenu sysMenu, List<SysMenu> menulist) {
//        ArrayList<SysMenu> ChildrenNodesList = new ArrayList<>();
//        //设置父节点的子节点集合
//        sysMenu.setChildren(ChildrenNodesList);
//        for(SysMenu it: menulist){
//            //找到子节点并将其加入集合中
//            if(it.getParentId()==sysMenu.getId()){
//                //调用递归寻找下一节点的子节点
//                sysMenu.getChildren().add(findChildren(it,menulist));
//            }
//        }
//        return sysMenu;
//    }

    /**
     * 使用递归方法建菜单
     * @param sysMenuList
     * @return
     */
    public static List<SysMenu> buildTree(List<SysMenu> sysMenuList) {
        List<SysMenu> trees = new ArrayList<>();
        for (SysMenu sysMenu : sysMenuList) {
            if (sysMenu.getParentId().longValue() == 0) {
                trees.add(findChildren(sysMenu,sysMenuList));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     * @param treeNodes
     * @return
     */
    public static SysMenu findChildren(SysMenu sysMenu, List<SysMenu> treeNodes) {
        sysMenu.setChildren(new ArrayList<SysMenu>());
        for (SysMenu it : treeNodes) {
            if(sysMenu.getId().longValue() == it.getParentId().longValue()) {
                sysMenu.getChildren().add(findChildren(it,treeNodes));
            }
        }
        return sysMenu;
    }


}
