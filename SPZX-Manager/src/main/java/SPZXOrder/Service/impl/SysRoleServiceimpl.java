package SPZXOrder.Service.impl;

import SPZXOrder.Mapper.SysRoleMapper;
import SPZXOrder.Mapper.SysRoleUserMapper;
import SPZXOrder.Service.SysRoleService;
import com.atguigu.spzx.model.dto.system.SysRoleDto;
import com.atguigu.spzx.model.entity.system.SysRole;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SysRoleServiceimpl implements SysRoleService {

    @Autowired
    SysRoleMapper sysRoleMapper;
    @Autowired
    SysRoleUserMapper sysRoleUserMapper;

    @Override
    public PageInfo<SysRole> findByPage(SysRoleDto sysRoleDto, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum , pageSize) ;
        List<SysRole> sysRoleList = sysRoleMapper.findByPage(sysRoleDto) ;
        PageInfo<SysRole> pageInfo = new PageInfo(sysRoleList) ;
        return pageInfo;
    }

    @Override
    public void saveSysRole(SysRole sysRole) {
        sysRoleMapper.saveSysRole(sysRole);
    }

    @Override
    public void updatesysRole(SysRole sysRole) {
        sysRoleMapper.updatesysRole(sysRole);
    }

    @Override
    public void DeleteSysRole(Long sysRoleid) {
        sysRoleMapper.DeleteSysRole(sysRoleid);
    }

    @Override
    public Map<String,Object> findAllRoles(Long userId) {
        //将所有职位信息加入集合中
        List<SysRole> list=sysRoleMapper.findall();

        // 查询当前登录用户的角色数据
        List<Long> sysRoles = sysRoleUserMapper.findSysUserRoleByUserId(userId);
        Map<String,Object> objectObjectMap = new HashMap<>();
        objectObjectMap.put("allRolesList",list);
        objectObjectMap.put("sysUserRoles", sysRoles);
        return objectObjectMap;
    }
}