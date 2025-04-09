/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 09:50:02
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-09 17:33:06
 * @FilePath: user-service/src/main/java/org/learning/userservice/service/impl/UserServiceImpl.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.commonmodule.resp.Result;
import com.common.commonmodule.util.JwtUtil;
import com.common.commonmodule.util.SizeFormatter;
import org.learning.userservice.entity.UserEntity;
import org.learning.userservice.mapper.AuthMapper;
import org.learning.userservice.mapper.UserMapper;
import org.learning.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 王博
 * @since 2025-03-13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    private static final String KEY = "token_key";
    @Value("${file.user-dir}")
    private String userDir;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private UserMapper userMapper;
    @Value("${config.redisTimeout}")
    private Long redisTimeout;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AuthMapper authMapper;

    public static List<Long> sevenDayUinx() {
        LocalDate today = LocalDate.now();
        List<Long> unixTimestamps = new ArrayList<>(7);
        for (int i = 0; i < 7; i++) {
            LocalDate date = today.minusDays(i);
            Instant instant = date.atStartOfDay(ZoneId.systemDefault()).toInstant();
            unixTimestamps.add(instant.getEpochSecond());
        }
        return unixTimestamps;
    }

    public static Long monthUnix() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        Instant instant = firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return instant.getEpochSecond();
    }

    public static String[] sevenDay() {
        // 获取当前日期
        LocalDate today = LocalDate.now();
        // 创建一个长度为7的数组来存储日期字符串
        String[] dateStrings = new String[7];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");

        for (int i = 0; i < 7; i++) {
            // 计算从今天开始往回数第i天的日期
            LocalDate date = today.minusDays(i);
            // 将日期格式化为 MM-dd 并添加到数组中
            dateStrings[i] = date.format(formatter);
        }

        // 返回包含日期字符串的数组
        return dateStrings;
    }

    @Override
    public Result<?> userBlur(int pageNum, int pageSize, String word) {
        Page<UserEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        if (word != null && !word.isEmpty()) {
            queryWrapper.or(wrapper -> wrapper
                    .like("phone", word)

            );
        }
        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage, "请求成功");

    }

    @Override
    public Result<?> Database() {
        try {
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE()",
                    Integer.class
            );
            Long size = jdbcTemplate.queryForObject(
                    "SELECT IFNULL(SUM(data_length + index_length), 0) FROM information_schema.tables WHERE table_schema = DATABASE()",
                    Long.class
            );
            Map<String, Object> result = new HashMap<>();
            // 获取普通用户数量 (role字段是2)
            QueryWrapper<UserEntity> commonUserWrapper = Wrappers.<UserEntity>query().eq("role", 1);
            Long commonUserCount = userMapper.selectCount(commonUserWrapper);
            result.put("user", commonUserCount != null ? commonUserCount : 0);

            // 获取管理员数量 (role字段是1)
            QueryWrapper<UserEntity> adminWrapper = Wrappers.<UserEntity>query().eq("role", 0);
            Long adminCount = userMapper.selectCount(adminWrapper);
            result.put("admin", adminCount != null ? adminCount : 0);
            result.put("count", count != null ? count : 0);
            result.put("size", size != null ? SizeFormatter.formatSize(size) : "0 B");
            List<Long> timestamps = sevenDayUinx();
            String[] dateStrings = sevenDay();
            List<List<Object>> creat = new ArrayList<>();
            List<List<Object>> active = new ArrayList<>();
            int weekCreate = 0;
            int weekActive = 0;
            for (int i = 0; i <= 6; i++) {
                if (i == 0) {
                    QueryWrapper<UserEntity> createdWrapper = Wrappers.<UserEntity>query().between("created", timestamps.get(i), System.currentTimeMillis() / 1000);
                    QueryWrapper<UserEntity> lastLoginWrapper = Wrappers.<UserEntity>query().between("last_login", timestamps.get(i), System.currentTimeMillis() / 1000);
                    int createdCount = Math.toIntExact(userMapper.selectCount(createdWrapper));
                    int lastLoginCount = Math.toIntExact(userMapper.selectCount(lastLoginWrapper));
                    result.put("todayCreat", createdCount);
                    result.put("todayActive", lastLoginCount);
                    weekCreate = weekCreate + createdCount;
                    weekActive = weekActive + lastLoginCount;
                    List<Object> pair = new ArrayList<>();
                    pair.add(dateStrings[i]);
                    pair.add(createdCount);
                    creat.add(pair);
                    List<Object> pair1 = new ArrayList<>();
                    pair1.add(dateStrings[i]);
                    pair1.add(lastLoginCount);
                    active.add(pair1);
                } else {
                    QueryWrapper<UserEntity> createdWrapper = Wrappers.<UserEntity>query().between("created", timestamps.get(i), timestamps.get(i - 1));
                    QueryWrapper<UserEntity> lastLoginWrapper = Wrappers.<UserEntity>query().between("last_login", timestamps.get(i), timestamps.get(i - 1));
                    int createdCount = Math.toIntExact(userMapper.selectCount(createdWrapper));
                    int lastLoginCount = Math.toIntExact(userMapper.selectCount(lastLoginWrapper));
                    weekCreate = weekCreate + createdCount;
                    weekActive = weekActive + lastLoginCount;
                    List<Object> pair = new ArrayList<>();
                    pair.add(dateStrings[i]);
                    pair.add(createdCount);
                    creat.add(pair);
                    List<Object> pair1 = new ArrayList<>();
                    pair1.add(dateStrings[i]);
                    pair1.add(lastLoginCount);
                    active.add(pair1);
                }
            }
            Long monthUnix = monthUnix();
            QueryWrapper<UserEntity> createdWrapper = Wrappers.<UserEntity>query().between("created", monthUnix, System.currentTimeMillis() / 1000);
            QueryWrapper<UserEntity> lastLoginWrapper = Wrappers.<UserEntity>query().between("last_login", monthUnix, System.currentTimeMillis() / 1000);
            int createdCount = Math.toIntExact(userMapper.selectCount(createdWrapper));
            int lastLoginCount = Math.toIntExact(userMapper.selectCount(lastLoginWrapper));
            result.put("monthCreat", createdCount);
            result.put("monthActive", lastLoginCount);
            result.put("weekCreat", weekCreate);
            result.put("weekActive", weekActive);
            Collections.reverse(creat);
            Collections.reverse(active);
            result.put("creat", creat);
            result.put("active", active);
            return Result.success(result);
        } catch (Exception e) {
            return Result.failure("获取数据库信息失败" + e);
        }
    }

    public UserEntity getUserById(Integer id) {
        return this.getById(id);
    }

    /**
     * 手机号获取用户信息
     *
     * @param phone 用户手机号
     * @return 用户实体
     */
    @Override
    public UserEntity getUserByPhone(String phone) {
        return this.getOne(new QueryWrapper<UserEntity>().eq("phone", phone));
    }

    /**
     * ID获取用户信息
     *
     * @param ID 用户ID
     * @return 用户实体
     */
    @Override
    public UserEntity getUserById(int ID) {
        return this.getOne(new QueryWrapper<UserEntity>().eq("ID", ID).select("phone", "auth", "created", "last_login", "idNum", "img"));
    }

    /**
     * 分页获取所有用户
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    @Override
    public Page<UserEntity> getAllUserPagination(int pageNum, int pageSize) {

        // 创建QueryWrapper，这里可以添加额外的查询条件
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();

        // 执行分页查询
        Page<UserEntity> resultPage = new Page<>(pageNum, pageSize);
        resultPage = this.page(resultPage, null);
        // 返回分页结果
        return resultPage;
    }

    /**
     * @param userEntity 要删除的用户实体
     * @return 结果数字
     */

    @Override
    public Result<?> DelUserById(UserEntity userEntity) {
        int ID = userEntity.getId();
        UserEntity user = this.userMapper.selectById(ID);
        if (user != null) {

            int result = this.userMapper.deleteById(ID);
            if (result > 0) {
                return Result.success(String.format("删除ID：%d的用户成功", ID));
            } else {
                return Result.failure(202, String.format("删除ID：%d的用户失败", ID));
            }
        } else {
            return Result.failure(202, String.format("ID：%d的用户不存在", ID));
        }
    }

    @Override
    public Result<?> updateUser(UserEntity userEntity) {
        UserEntity user = this.getOne(new QueryWrapper<UserEntity>().eq("id", userEntity.getId()));
        if (user == null) {
            return Result.failure("用户不存在");
        }
        try {
            this.saveOrUpdate(userEntity);
            return Result.success("更新成功");
        } catch (Exception e) {
            return Result.failure("更新失败" + e.getMessage());
        }
    }

    @Override
    public Result<?> updateImg(MultipartFile file, String name, int id) {
        String projectRoot = System.getProperty("user.dir");
        // 拼接完整路径
        String fullUploadDir = projectRoot + "/" + this.userDir;
        Path path = Paths.get(fullUploadDir);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                return Result.failure(202, "无法创建目录: " + path);
            }
        }
        if (file == null || file.isEmpty()) {
            return Result.failure("文件为空，请重新上传");
        }
        String oriName = this.getOne(new QueryWrapper<UserEntity>().eq("ID", id)).getImg();
        Path newPath = path.resolve(name);


        if (oriName == null) {
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, newPath, StandardCopyOption.REPLACE_EXISTING);
                UserEntity user = new UserEntity();
                user.setImg(name);
                user.setId(id);
                this.updateById(user);
                return Result.success("该文件与文件数据更新成功: " + name);

            } catch (InterruptedIOException e) {
                // 处理由于线程中断引起的IO异常
                Thread.currentThread().interrupt(); // 重新设置中断状态
                return Result.failure(203, "文件上传被中断: " + name + "请重试");
            } catch (IOException e) {
                return Result.failure(202, "该文件上传失败：" + name + "错误：" + e);
            }
        } else {
            Path targetLocation = path.resolve(oriName);
            try (InputStream inputStream = file.getInputStream()) {
                boolean isDeleted = Files.deleteIfExists(targetLocation);
                Files.copy(inputStream, newPath, StandardCopyOption.REPLACE_EXISTING);
                if (isDeleted) {
                    UserEntity user = new UserEntity();
                    user.setImg(name);
                    user.setId(id);
                    this.updateById(user);
                    return Result.success("该文件与文件数据更新成功: " + name);
                } else {
                    return Result.success("该文件更新成功但无法删除原文件: " + name);
                }
            } catch (InterruptedIOException e) {
                // 处理由于线程中断引起的IO异常
                Thread.currentThread().interrupt(); // 重新设置中断状态
                return Result.failure(203, "文件上传被中断: " + name + "请重试");
            } catch (IOException e) {
                return Result.failure(202, "该文件上传失败：" + name + "错误：" + e);
            }
        }

    }

    @Override
    public Result<?> resetPwd(String phone, String pwd, String oldPwd) {
        UserEntity user = this.getOne(new QueryWrapper<UserEntity>().eq("phone", phone));
        if (user == null) {
            return Result.failure("手机号不存在，请先注册");
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        if (!bCryptPasswordEncoder.matches(oldPwd, user.getPwd())) {
            return Result.failure("旧密码错误");
        }
        // 加密密码
        String encodedPassword = bCryptPasswordEncoder.encode(pwd);
        user.setPwd(encodedPassword);
        // 保存用户
        this.saveOrUpdate(user, new QueryWrapper<UserEntity>().eq("phone", phone));
        return Result.success("修改成功请返回登录");
    }

    @Override
    public Result<?> resetPhone(String oldPhone, String newPhone, String pwd) {
        UserEntity user = this.getOne(new QueryWrapper<UserEntity>().eq("phone", oldPhone));
        UserEntity user1 = this.getOne(new QueryWrapper<UserEntity>().eq("phone", newPhone));

        if (user == null) {

            return Result.failure("手机号不存在");
        }
        if (user1 != null) {
            return Result.failure("要修改的手机号已存在");
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        if (!bCryptPasswordEncoder.matches(pwd, user.getPwd())) {
            return Result.failure("密码错误");
        }
        user.setPhone(newPhone);
        this.saveOrUpdate(user, new QueryWrapper<UserEntity>().eq("phone", oldPhone));
        return Result.success("修改成功请返回登录");

    }

    /**
     * 用户登录
     *
     * @param phone 手机号
     * @return 登录结果
     */
    @Override
    public Result<?> login(String phone) {
        Map<String, Object> result = new HashMap<>();
        UserEntity user = this.getUserByPhone(phone);
        String outh = user.getAuth();
        int id = user.getId();
        result.put("outh", outh);
        result.put("id", id);
        result.put("sex", user.getSex());
        result.put("phone", user.getPhone());
        result.put("username", user.getUsername());
        long date = System.currentTimeMillis() / 1000;
        user.setLastLogin(date);
        this.updateById(user);
        if (outh.equals("管理")) {
            String userId = String.valueOf(user.getId());
//        jwt生成token
            String token = JwtUtil.getToken(userId);
//        将token存入redis
            this.redisTemplate.opsForValue().set(token, userId, this.redisTimeout, TimeUnit.HOURS);
            result.put("token", token);

            return Result.success(result, "登录成功！即将跳转...");
        } else {
            return Result.failure(202, "权限不足");
        }
    }

    /**
     * 用户注册
     *
     * @param userEntity 用户实体
     * @return 注册结果
     */
    @Override
    public Result<?> register(UserEntity userEntity) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", userEntity.getPhone());
        UserEntity existingUser = this.getOne(queryWrapper);
        long time = System.currentTimeMillis() / 1000;
        if (existingUser != null) {
            return Result.failure(202, "手机号已存在");
        } else {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

            // 加密密码
            String encodedPassword = bCryptPasswordEncoder.encode(userEntity.getPwd());
            userEntity.setPwd(encodedPassword);

            userEntity.setRole(1); // 默认角色为普通用户
            userEntity.setAuth("用户"); // 默认权限为普通用户
            userEntity.setCreated(time);
            // 保存用户
            this.save(userEntity);
            return Result.success("注册成功");
        }

    }
}