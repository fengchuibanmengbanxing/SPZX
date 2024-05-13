package aspect;

import Service.AsyncOperLogService;
import Service.annotation.Log;
import com.atguigu.spzx.model.entity.system.SysOperLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.LogUtil;

/**
 * @Author 清醒
 * @Date 2024/5/3 13:29
 */
@Aspect
@Component
@Slf4j
public class LogAspect {     // 环绕通知切面类定义

    @Autowired
    private AsyncOperLogService asyncOperLogService;

    @Around(value = "@annotation(sysLog)")
    public Object doAroundAdvice(ProceedingJoinPoint joinPoint, Log sysLog) {

        // 构建前置参数
        SysOperLog sysOperLog = new SysOperLog();

        LogUtil.beforeHandleLog(sysLog, joinPoint, sysOperLog);

        Object proceed = null;


        try {
            proceed = joinPoint.proceed();// 执行业务方法
            LogUtil.afterHandlLog(sysLog , proceed , sysOperLog , 0 , null) ;
        } catch (Throwable e) {// 代码执行进入到catch中，业务方法执行产生异常
            e.printStackTrace();
            LogUtil.afterHandlLog(sysLog , proceed , sysOperLog , 1 , null) ;
            throw new RuntimeException(e);
        }

        // 保存日志数据
        asyncOperLogService.saveSysOperLog(sysOperLog);

        return proceed;                                // 返回执行结果
    }
}