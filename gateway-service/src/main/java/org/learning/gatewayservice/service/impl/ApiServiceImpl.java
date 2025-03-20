/**
 * @Author: wangbo 3812943352@qq.com
 * @Date: 2024-11-11 14:20:59
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-17 11:19:11
 * @FilePath: gateway-service/src/main/java/org/learning/gatewayservice/service/impl/ApiServiceImpl.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.gatewayservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.learning.gatewayservice.entity.ApiEntity;
import org.learning.gatewayservice.entity.UserEntity;
import org.learning.gatewayservice.mapper.ApiMapper;
import org.learning.gatewayservice.mapper.UserMapper;
import org.learning.gatewayservice.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 王博
 * @since 2024-11-09
 */
@Service
public class ApiServiceImpl extends ServiceImpl<ApiMapper, ApiEntity> implements ApiService {
    @Autowired
    private ApiMapper apiMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<String> getApi() {
        return apiMapper.getAllApi();
    }

    @Override
    public boolean addVisit(String api) {

        ApiEntity apiEntity = this.getOne(new QueryWrapper<ApiEntity>().eq("api", api));
        apiEntity.setVisits(apiEntity.getVisits() + 1);

        return this.updateById(apiEntity);


    }

    @Override
    public boolean addTimes(String api) {
        ApiEntity apiEntity = this.getOne(new QueryWrapper<ApiEntity>().eq("api", api));
        apiEntity.setTimes(apiEntity.getTimes() + 1);
        return this.updateById(apiEntity);
    }

    @Override
    public boolean addError(String api) {
        ApiEntity apiEntity = this.getOne(new QueryWrapper<ApiEntity>().eq("api", api));
        apiEntity.setErrors(apiEntity.getErrors() + 1);
        return this.updateById(apiEntity);
    }

    @Override
    public boolean apiUserList(String api, String token) {
        List<String> outhList = redisTemplate.opsForList().range("authList", 0, -1);
        int ID = (Integer) redisTemplate.opsForValue().get(token);
        UserEntity user = userMapper.selectById(ID);
        if (user == null) {
            return false;
        }
        if (Objects.equals(user.getAuth(), "用户")) {
            return outhList.contains(api) && outhList != null;
        } else {
            return true;
        }
    }


    @Override
    public boolean apiShouldNot(String api) {
        // 假设 RedisTemplate 已经配置为使用 StringRedisSerializer
        List<String> outhList = redisTemplate.opsForList().range("loginList", 0, -1);

        if (outhList != null && !outhList.isEmpty()) {
            // 修改此处，检查api是否以outhList中的任意一个路径作为前缀
            boolean shouldAccess = outhList.stream().anyMatch(prefix -> api.startsWith(prefix));

            System.out.println("api = " + api + " outhList = " + outhList + " match = " + shouldAccess);

            return shouldAccess;
        } else {
            return false;
        }
    }

}

