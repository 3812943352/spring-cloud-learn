/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 09:01:36
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-24 16:14:03
 * @FilePath: contents-service/src/main/java/org/learning/contentsservice/service/impl/ATypeServiceImpl.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.contentsservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.commonmodule.resp.Result;
import org.learning.contentsservice.entity.ATypeEntity;
import org.learning.contentsservice.entity.ArticleEntity;
import org.learning.contentsservice.mapper.ATypeMapper;
import org.learning.contentsservice.mapper.ArticleMapper;
import org.learning.contentsservice.service.ATypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 王博
 * @since 2025-03-14
 */
@Service
public class ATypeServiceImpl extends ServiceImpl<ATypeMapper, ATypeEntity> implements ATypeService {
    private final ATypeMapper aTypeMapper;
    private final ArticleMapper articleMapper;


    public ATypeServiceImpl(ATypeMapper aTypeMapper, ArticleMapper articleMapper) {
        this.aTypeMapper = aTypeMapper;
        this.articleMapper = articleMapper;
    }

    @Override
    public Result<?> addType(ATypeEntity aTypeEntity) {
        if (aTypeEntity.getType() == null) {
            return Result.failure("文章类别不能为空");
        }
        if (this.getOne(new QueryWrapper<ATypeEntity>().eq("type", aTypeEntity.getType())) != null) {
            return Result.failure("文章类别已存在");
        }
        this.save(aTypeEntity);
        return Result.success(aTypeEntity.getType() + "添加成功");
    }

    @Override
    public Result<?> updateType(ATypeEntity aTypeEntity) {
        if (aTypeEntity.getType() == null) {
            return Result.failure("文章类别不能为空");
        }
        if (this.getOne(new QueryWrapper<ATypeEntity>().eq("type", aTypeEntity.getType())) == null) {
            return Result.failure("文章类别不存在");
        }
        this.updateById(aTypeEntity);
        return Result.success(aTypeEntity.getType() + "修改成功");
    }

    @Override
    public Result<?> deleteType(Integer id) {
        if (this.getOne(new QueryWrapper<ATypeEntity>().eq("id", id)) == null) {
            return Result.failure("文章类别不存在");
        }
        List<ArticleEntity> articleEntities = articleMapper.selectList(new QueryWrapper<ArticleEntity>().eq("type", id));
        if (!articleEntities.isEmpty()) {
            return Result.failure("该课程类别下有课程" + articleEntities + "，无法删除");
        }
        this.removeById(id);
        return Result.success("删除成功");
    }

    @Override
    public Result<?> getType(Integer pageNum, Integer pageSize) {
        try {
            Page<ATypeEntity> resultPage = new Page<>(pageNum, pageSize);
            QueryWrapper<ATypeEntity> queryWrapper = new QueryWrapper<>();
            resultPage = this.page(resultPage, queryWrapper);
            return Result.success(resultPage);
        } catch (Exception e) {
            return Result.failure(202, "查询失败" + e.getMessage());
        }
    }

    @Override
    public Result<?> listType() {
        List<ATypeEntity> list = this.list(new QueryWrapper<>());
        return Result.success(list);
    }
}
