/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 16:01:42
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-08 17:27:51
 * @FilePath: course-service/src/main/java/org/learning/courseservice/service/impl/ERecordServiceImpl.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.commonmodule.resp.Result;
import org.learning.courseservice.entity.ERecordEntity;
import org.learning.courseservice.mapper.ERecordMapper;
import org.learning.courseservice.service.ERecordService;
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
public class ERecordServiceImpl extends ServiceImpl<ERecordMapper, ERecordEntity> implements ERecordService {
    @Override
    public Result<?> get(int pageNum, int pageSize) {
        try {
            Page<ERecordEntity> resultPage = new Page<>(pageNum, pageSize);
            QueryWrapper<ERecordEntity> queryWrapper = new QueryWrapper<>();
            resultPage = this.page(resultPage, queryWrapper);
            return Result.success(resultPage);
        } catch (Exception e) {
            return Result.failure(202, "查询失败" + e);
        }
    }

    @Override
    public Result<?> blur(int pageNum, int pageSize, String word) {
        Page<ERecordEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<ERecordEntity> queryWrapper = new QueryWrapper<>();
        if (word != null && !word.isEmpty()) {
            queryWrapper.or(wrapper -> wrapper
                    .like("user", word)

            );
        }
        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage, "请求成功");
    }

    @Override
    public Result<?> date(int pageNum, int pageSize, long startTime, long endTime) {
        Page<ERecordEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<ERecordEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("created", startTime, endTime);
        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage);
    }

    @Override
    public Result<?> userRecord(String user) {
        List<ERecordEntity> eRecordEntityList = this.list(new QueryWrapper<ERecordEntity>().eq("user", user));

        return Result.success(eRecordEntityList);
    }
}
