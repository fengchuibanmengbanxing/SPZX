package SPZXOrder.Mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRoleUserMapper {
    void deleteByUserId(Long userId);


    void dodoAssign( Long s, Long userId);

    List<Long> findSysUserRoleByUserId(Long userId);

}
