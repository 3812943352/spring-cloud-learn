/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 09:55:42
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-14 11:05:33
 * @FilePath: user-service/src/main/java/org/learning/userservice/service/impl/AuthServiceImpl.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.commonmodule.resp.Result;
import org.learning.userservice.entity.AuthEntity;
import org.learning.userservice.mapper.AuthMapper;
import org.learning.userservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 王博
 * @since 2025-03-13
 */
@Service
public class AuthServiceImpl extends ServiceImpl<AuthMapper, AuthEntity> implements AuthService {
    @Autowired
    private AuthMapper authMapper;

    @Override
    public Result<?> getAuth(int pageNum, int pageSize) {
        try {
            Page<AuthEntity> resultPage = new Page<>(pageNum, pageSize);
            QueryWrapper<AuthEntity> queryWrapper = new QueryWrapper<>();
            resultPage = this.page(resultPage, queryWrapper);
            return Result.success(resultPage);

        } catch (Exception e) {
            return Result.failure(202, "查询失败" + e.getMessage());
        }
    }

    @Override
    public Result<?> addAuth(AuthEntity authEntity) {
        try {
            AuthEntity auth = this.getOne(new QueryWrapper<AuthEntity>().eq("path", authEntity.getPath()));
            if (auth != null) {
                return Result.failure(202, "请勿重复添加");
            }
            this.save(authEntity);
            return Result.success(authEntity.getPath() + "添加成功");
        } catch (Exception e) {
            return Result.failure(202, "添加失败" + e.getMessage());
        }
    }

    @Override
    public Result<?> delAuth(AuthEntity authEntity) {
        try {
            this.removeById(authEntity);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.failure(202, "删除失败" + e.getMessage());
        }
    }

    @Override
    public Result<?> updateAuth(AuthEntity authEntity) {
        try {
            this.saveOrUpdate(authEntity);
            return Result.success("更新成功");
        } catch (Exception e) {
            return Result.failure(202, "更新失败" + e.getMessage());
        }
    }
}
