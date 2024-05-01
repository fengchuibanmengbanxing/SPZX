package SPZX;

import com.atguigu.spzx.model.entity.system.SysUser;

public class AuthContextUtil {
/**
 * 完成该功能需要使用到ThreadLocal，ThreadLocal是jdk所提供的一个线程工具类，叫做线程变量，
 * 意思是ThreadLocal中填充的变量属于当前线程，该变量对其他线程而言是隔离的，
 * 也就是说该变量是当前线程独有的变量，使用该工具类可以实现在同一个线程进行数据的共享。
 * */
    private static final ThreadLocal<SysUser> threadLocal = new ThreadLocal<>();


    public static void set(SysUser sysUser) {
        threadLocal.set(sysUser);
    }

    public static SysUser get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }

}
