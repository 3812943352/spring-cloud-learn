/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 14:16:00
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-13 15:00:02
 * @FilePath: ApiSupervision-service/src/main/java/org/learning/apisupervisionservice/service/impl/IpServiceImpl.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.apisupervisionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.commonmodule.resp.Result;
import org.learning.apisupervisionservice.entity.IpEntity;
import org.learning.apisupervisionservice.mapper.IpMapper;
import org.learning.apisupervisionservice.service.IpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
public class IpServiceImpl extends ServiceImpl<IpMapper, IpEntity> implements IpService {


    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public Result<?> getBan(int pageNum, int pageSize) {
        Page<IpEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<IpEntity> queryWrapper = new QueryWrapper<>();
        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage);
    }


    @Override
    public Result<?> setBan(String ip, String reason, Long time) {
        try {
            // 创建并保存IP实体
            long bantime = Instant.now().toEpochMilli();
            IpEntity ipEntity = new IpEntity();
            ipEntity.setIp(ip);
            ipEntity.setReason(reason);
            ipEntity.setTime(time);
            ipEntity.setBantime(bantime);
            this.save(ipEntity);
            // 设置Redis中的键值对
            if (time == 0) {
                // 如果时间是0，表示永不过期
                redisTemplate.opsForValue().set(ip, time);
            } else {
                // 设置有指定过期时间的键值对
                redisTemplate.opsForValue().set(ip, time, time / 1000, TimeUnit.SECONDS);
            }
            return Result.success();
        } catch (Exception e) {
            return Result.failure(202, "封禁失败: " + e.getMessage());
        }
    }


    @Override
    public Result<?> unBan(String ip) {
        try {
            this.remove(new QueryWrapper<IpEntity>().eq("ip", ip));
            redisTemplate.delete(ip);
            return Result.success();
        } catch (Exception e) {
            return Result.failure(202, "解封失败: " + e.getMessage());
        }
    }

    @Override
    public Result<?> updateBan(String ip, String reason, Long time) {
        try {
            IpEntity ipEntity = new IpEntity();
            ipEntity.setReason(reason);
            ipEntity.setTime(time);
            this.update(new QueryWrapper<IpEntity>().eq("ip", ip));
            redisTemplate.delete(ip);
            if (time == 0) {
                // 如果时间是0，表示永不过期
                redisTemplate.opsForValue().set(ip, time);
            } else {
                // 设置有指定过期时间的键值对
                redisTemplate.opsForValue().set(ip, time, time / 1000, TimeUnit.SECONDS);
            }
            return Result.success();
        } catch (Exception e) {
            return Result.failure(202, "封禁失败: " + e.getMessage());
        }
    }

    @Override
    public Result<?> getBlur(int pageNum, int pageSize, String word) {
        Page<IpEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<IpEntity> queryWrapper = new QueryWrapper<>();
        if (word != null && !word.isEmpty()) {
            queryWrapper.or(wrapper -> wrapper
                    .like("ip", word)
                    .or().like("reason", word)

            );
        }
        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage, "请求成功");
    }

    @Override
    public Result<?> getDate(int pageNum, int pageSize, long startTime, long endTime) {
        Page<IpEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<IpEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("bantime", startTime, endTime);
        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage);
    }
}