package SPZXOrder.Service.impl;

import SPZXOrder.Exception.GuiguException;
import SPZXOrder.Mapper.SysRoleUserMapper;
import SPZXOrder.Mapper.SysUserMapper;
import SPZXOrder.Service.SysUserService;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.atguigu.spzx.model.dto.system.AssginRoleDto;
import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.dto.system.SysUserDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.system.LoginVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SysUserServiceimpl implements SysUserService {

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    SysRoleUserMapper sysRoleUserMapper;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public SysUser getUserInfo(String token) {
        String userJson = stringRedisTemplate.opsForValue().get("user:login:" + token);
        return JSON.parseObject(userJson, SysUser.class);
    }

    @Override
    public LoginVo login(LoginDto loginDto) {
        //通过LoginDto获取验证码
        String captcha = loginDto.getCaptcha();
        String codeKey = loginDto.getCodeKey();
        //验证验证码与redis中存储的是否一致
        String s = stringRedisTemplate.opsForValue()
                .get("user:login:validatecode:" + codeKey);

        if (StrUtil.isEmpty(s) || !StrUtil.endWithIgnoreCase(s, captcha)) {
            throw new GuiguException(ResultCodeEnum.VALIDATECODE_ERROR);
        }
        //一致则删除redis中的键值对执行下面代码

        stringRedisTemplate.delete("user:login:validatecode:" + codeKey);
        String userName = loginDto.getUserName();
        //1 根据传入dto获取用户名查询数据库
        SysUser sysUser = sysUserMapper.selectByUserName(userName);
        //2 查询数据库中是否存在用户输入用户名并获取对象
        //3 查询数据库中是否存在用户输入用户名存在不存在返回错误信息
        if (sysUser == null) {
//            throw new RuntimeException("用户名错误");
            throw new GuiguException(ResultCodeEnum.LOGIN_ERROR);
        }

        //3 比较输入的密码与数据库密码  获取对象.getpassword
        String password = loginDto.getPassword();
        String database_password = sysUser.getPassword();
        //4 不一致则返回
        // 将输入的密码进行加密后与数据库中密码比较
        String md5InputPassword = DigestUtils.md5DigestAsHex(password.getBytes());

        if (!database_password.equals(md5InputPassword)) {
            throw new GuiguException(ResultCodeEnum.LOGIN_ERROR);
        }
        //5 生成用户token存入redis中
        //默认获取的uuid是有下划线的需要去除  也可以直接使用雪花算法
        String token = UUID.randomUUID().toString().replace("-", "");
//        JSON.toJSONString(sysUser)  将对象转化为json格式存储在redis中
        stringRedisTemplate.opsForValue().
                set("user:login:" + token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);

        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        loginVo.setRefresh_token("");
        return loginVo;
    }

    @Override
    public void logout(String token) {
        stringRedisTemplate.delete("user:login:" + token);
    }

    @Override
    public PageInfo<SysUser> findPage(Integer pageNum, Integer pageSize, SysUserDto sysUserDto) {
        PageHelper.startPage(pageNum, pageSize);
        List<SysUser> sysUserList = sysUserMapper.findPage(sysUserDto);
        PageInfo<SysUser> sysUserPageInfo = new PageInfo<>(sysUserList);
        return sysUserPageInfo;
    }

    @Override
    public void save(SysUser sysUser) {
        SysUser dbUser = sysUserMapper.findbyname(sysUser.getUserName());
        //查询数据库中是否已经存在
        if (dbUser != null) {
            //存在则抛出异常
            throw new GuiguException(ResultCodeEnum.USER_NAME_IS_EXISTS);
        }
        //对用户密码进行加密
        String password = sysUser.getPassword();
        String digestPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        sysUser.setPassword(digestPassword);
        //将用户状态设置为逻辑未删除
        sysUser.setStatus(0);

        sysUserMapper.save(sysUser);


    }

    @Override
    public void update(SysUser sysUser) {
        sysUserMapper.update(sysUser);
    }

    @Override
    public void delete(Integer userId) {
        sysUserMapper.delete(userId);
    }

    @Override
    public void doAssgin(AssginRoleDto assginRoleDto) {
        //根据AssginRoleDto 删除关联数据库中已有的职位关系
        Long userId = assginRoleDto.getUserId();
        sysRoleUserMapper.deleteByUserId(userId);
        //将前端传入AssginRoleDto中的list职位集合加入数据库
        List<Long> roleIdList = assginRoleDto.getRoleIdList();
        for (Long s : roleIdList) {
            sysRoleUserMapper.dodoAssign(s,assginRoleDto.getUserId());
        }
    }
}
