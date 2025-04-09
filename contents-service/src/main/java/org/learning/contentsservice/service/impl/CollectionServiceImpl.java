/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 09:02:37
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-01 11:30:33
 * @FilePath: contents-service/src/main/java/org/learning/contentsservice/service/impl/CollectionServiceImpl.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.contentsservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.commonmodule.resp.Result;
import org.learning.contentsservice.entity.ArticleEntity;
import org.learning.contentsservice.entity.CollectionEntity;
import org.learning.contentsservice.entity.LikeEntity;
import org.learning.contentsservice.mapper.ArticleMapper;
import org.learning.contentsservice.mapper.CollectionMapper;
import org.learning.contentsservice.service.CollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 王博
 * @since 2025-03-14
 */
@Service
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper, CollectionEntity> implements CollectionService {
    private final CollectionMapper collectionMapper;
    private final ArticleMapper articleMapper;

    public CollectionServiceImpl(CollectionMapper collectionMapper, ArticleMapper articleMapper) {
        this.collectionMapper = collectionMapper;
        this.articleMapper = articleMapper;
    }

    @Override
    public Result<?> add(CollectionEntity collectionEntity) {
        // 获取当前时间戳（秒级）
        long date = System.currentTimeMillis() / 1000;
        collectionEntity.setCreated(date);

        // 查询文章是否存在
        ArticleEntity articleEntity = articleMapper.selectOne(
                new QueryWrapper<ArticleEntity>().eq("id", collectionEntity.getArticle())
        );

        if (articleEntity == null) {
            return Result.failure("文章不存在，无法添加收藏");
        }

        // 保存评论
        boolean isSaved = collectionMapper.insert(collectionEntity) > 0;

        if (isSaved) {
            // 更新文章的评论数
            int updatedRows = articleMapper.update(null,
                    new UpdateWrapper<ArticleEntity>()
                            .setSql("collection = collection + 1")
                            .eq("id", collectionEntity.getArticle()));

            if (updatedRows <= 0) {
                return Result.failure("更新文章评论数失败");
            }
        } else {
            return Result.failure("评论保存失败");
        }

        return Result.success("评论成功");
    }


    @Override
    public Result<?> del(int id) {
        // 1. 查询要删除的评论
        CollectionEntity collection = collectionMapper.selectOne(
                new QueryWrapper<CollectionEntity>().eq("id", id)
        );

        if (collection == null) {
            return Result.failure("评论不存在，无法删除");
        }

        // 2. 删除评论
        int deletedRows = collectionMapper.delete(
                new QueryWrapper<CollectionEntity>().eq("id", id)
        );

        if (deletedRows <= 0) {
            return Result.failure("评论删除失败");
        }

        // 3. 更新对应文章的评论数（comment字段减一）
        int updatedRows = articleMapper.update(null,
                new UpdateWrapper<ArticleEntity>()
                        .setSql("comment = comment - 1")
                        .eq("id", collection.getArticle())
                        // 确保comment字段不会小于0
                        .gt("comment", 0)
        );

        if (updatedRows <= 0) {
            return Result.failure("更新文章评论数失败");
        }

        return Result.success("删除成功");
    }

    @Override
    public Result<?> get(int pageNum, int pageSize) {
        try {
            Page<CollectionEntity> resultPage = new Page<>(pageNum, pageSize);
            QueryWrapper<CollectionEntity> queryWrapper = new QueryWrapper<>();
            resultPage = this.page(resultPage, queryWrapper);
            return Result.success(resultPage);
        } catch (Exception e) {
            return Result.failure(202, "查询失败" + e.getMessage());
        }
    }

    @Override
    public Result<?> blur(int pageNum, int pageSize, String word) {
        Page<CollectionEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<CollectionEntity> queryWrapper = new QueryWrapper<>();
        if (word != null && !word.isEmpty()) {
            queryWrapper.or(wrapper -> wrapper
                    .like("user", word)
                    .or().like("article", word)

            );
        }
        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage, "请求成功");
    }

    @Override
    public Result<?> date(int pageNum, int pageSize, long startTime, long endTime) {
        Page<CollectionEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<CollectionEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("created", startTime, endTime);
        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage);
    }
}
