/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 16:01:42
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-18 16:45:11
 * @FilePath: course-service/src/main/java/org/learning/courseservice/service/impl/PaperServiceImpl.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.commonmodule.resp.Result;
import org.learning.courseservice.entity.CourseEntity;
import org.learning.courseservice.entity.PaperEntity;
import org.learning.courseservice.mapper.CourseMapper;
import org.learning.courseservice.mapper.PaperMapper;
import org.learning.courseservice.service.PaperService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 王博
 * @since 2025-03-14
 */
@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, PaperEntity> implements PaperService {


    private final CourseMapper courseMapper;

    public PaperServiceImpl(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    @Override
    public Result<?> addPaper(PaperEntity paperEntity) {
        try {
            Integer courseID = paperEntity.getCourse();
            CourseEntity courseEntity = courseMapper.selectOne(new QueryWrapper<CourseEntity>().eq("id", courseID));
            if (courseEntity == null) {
                return Result.failure(202, "课程不存在");
            }
            if (courseEntity.getPaper() == 1) {
                return Result.failure(202, "该课程已添加试卷");
            }
            courseEntity.setPaper(1);
            long date = System.currentTimeMillis() / 1000;
            paperEntity.setCreated(date);
            paperEntity.setUpdated(date);
            paperEntity.setTopics("[]");
            courseMapper.updateById(courseEntity);
            boolean save = save(paperEntity);
            if (save) {
                return Result.success(200, "添加成功");
            }
            return Result.failure(202, "添加失败");
        } catch (Exception e) {
            return Result.failure(202, "添加失败" + e.getMessage());
        }
    }

    @Override
    public Result<?> updatePaper(PaperEntity paperEntity) {
        boolean update = updateById(paperEntity);
        if (update) {
            return Result.success(200, "修改成功");
        }
        return Result.failure(202, "修改失败");
    }


    @Override
    public Result<?> delPaper(int id, int course) {
        CourseEntity courseEntity = courseMapper.selectOne(new QueryWrapper<CourseEntity>().eq("id", course));
        courseEntity.setPaper(0);
        courseMapper.updateById(courseEntity);
        boolean removeById = removeById(id);
        if (removeById) {
            return Result.success(200, "删除成功");
        }
        return Result.failure(202, "删除失败");
    }


    @Override
    public Result<?> get(int pageNum, int pageSize) {
        try {
            Page<PaperEntity> resultPage = new Page<>(pageNum, pageSize);
            QueryWrapper<PaperEntity> queryWrapper = new QueryWrapper<>();
            resultPage = this.page(resultPage, queryWrapper);
            return Result.success(resultPage);
        } catch (Exception e) {
            return Result.failure(202, "查询失败" + e.getMessage());
        }
    }

    @Override
    public Result<?> blur(int pageNum, int pageSize, String word) {
        Page<PaperEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<PaperEntity> queryWrapper = new QueryWrapper<>();
        if (word != null && !word.isEmpty()) {
            queryWrapper.or(wrapper -> wrapper
                    .like("name", word)

            );
        }
        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage, "请求成功");
    }

    @Override
    public Result<?> date(int pageNum, int pageSize, long startTime, long endTime) {
        Page<PaperEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<PaperEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("created", startTime, endTime);
        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage);
    }

    @Override
    public Result<?> listCourse() {
        List<PaperEntity> paperEntities = this.list(new QueryWrapper<PaperEntity>().select("ID", "name"));

        List<Map<String, Object>> formattedList = new ArrayList<>();
        for (PaperEntity paper : paperEntities) {
            Map<String, Object> map = new HashMap<>();
            map.put("value", paper.getId());
            map.put("label", paper.getName());
            formattedList.add(map);
        }

        return Result.success(formattedList);
    }
}
