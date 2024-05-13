package SPZXOrder.Mapper;

import com.atguigu.spzx.model.entity.system.SysOperLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author 清醒
 * @Date 2024/5/3 14:10
 */
@Mapper
public interface SysOperLogMapper {
    //保存日志信息
    void insert(SysOperLog sysOperLog);

}
