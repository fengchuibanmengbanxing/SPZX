package Service;

import com.atguigu.spzx.model.entity.system.SysOperLog;

/**
 * @Author 清醒
 * @Date 2024/5/3 14:09
 */
public interface AsyncOperLogService {			// 保存日志数据
    public abstract void saveSysOperLog(SysOperLog sysOperLog) ;
}
