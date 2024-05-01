package SPZX.Service;

import com.atguigu.spzx.model.dto.system.AssginRoleDto;
import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.dto.system.SysUserDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.system.LoginVo;
import com.github.pagehelper.PageInfo;

public interface SysUserService {


    SysUser getUserInfo(String token);

    LoginVo login(LoginDto loginDto);

    void logout(String token);

    PageInfo<SysUser> findPage(Integer pageNum, Integer pageSize, SysUserDto sysUserDto);

    void save(SysUser sysUser);

    void update(SysUser sysUser);

    void delete(Integer userId);

    void doAssgin(AssginRoleDto assginRoleDto);

}
