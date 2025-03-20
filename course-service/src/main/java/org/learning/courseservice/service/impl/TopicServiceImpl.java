/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 16:01:42
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-20 10:40:01
 * @FilePath: course-service/src/main/java/org/learning/courseservice/service/impl/TopicServiceImpl.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.commonmodule.resp.Result;
import org.learning.courseservice.entity.TopicEntity;
import org.learning.courseservice.mapper.TopicMapper;
import org.learning.courseservice.service.TopicService;
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
public class TopicServiceImpl extends ServiceImpl<TopicMapper, TopicEntity> implements TopicService {

    @Override
    public Result<?> addTopic(TopicEntity topicEntity) {
        if (this.save(topicEntity)) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }

    @Override
    public Result<?> updateTopic(TopicEntity topicEntity) {
        try {
            this.updateById(topicEntity);
            return Result.success();
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }

    }

    @Override
    public Result<?> deleteTopic(Integer id) {
        if (this.removeById(id)) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }

    @Override
    public Result<?> get(int pageNum, int pageSize, int type) {
        try {
            Page<TopicEntity> resultPage = new Page<>(pageNum, pageSize);
            QueryWrapper<TopicEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("type", type);
            resultPage = this.page(resultPage, queryWrapper);
            return Result.success(resultPage);
        } catch (Exception e) {
            return Result.failure(202, "查询失败" + e.getMessage());
        }
    }

    @Override
    public Result<?> blur(int pageNum, int pageSize, String word, String type) {
        Page<TopicEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<TopicEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type);
        if (word != null && !word.isEmpty()) {
            queryWrapper.or(wrapper -> wrapper
                    .like("stem", word)
            );
        }
        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage, "请求成功");
    }
}
