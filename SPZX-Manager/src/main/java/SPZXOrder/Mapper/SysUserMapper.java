package SPZXOrder.Mapper;

import com.atguigu.spzx.model.dto.system.SysUserDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserMapper {


//获取数据库中对象
    public abstract SysUser selectByUserName( String userName);

    List<SysUser> findPage(SysUserDto sysUserDto);

    void save(SysUser sysUser);

    SysUser findbyname(String userName);

    void update(SysUser sysUser);

    void delete(Integer userId);
}
