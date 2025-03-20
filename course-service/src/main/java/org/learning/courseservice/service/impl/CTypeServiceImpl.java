/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-18 21:24:41
 * @FilePath: course-service/src/main/java/org/learning/courseservice/service/impl/CTypeServiceImpl.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.commonmodule.resp.Result;
import org.learning.courseservice.entity.CourseEntity;
import org.learning.courseservice.entity.PaperEntity;
import org.learning.courseservice.mapper.CTypeMapper;
import org.learning.courseservice.entity.CTypeEntity;
import org.learning.courseservice.mapper.CourseMapper;
import org.learning.courseservice.service.CTypeService;
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
 * @since 2025-03-13
 */
@Service
public class CTypeServiceImpl extends ServiceImpl<CTypeMapper, CTypeEntity> implements CTypeService {

    private final CourseMapper courseMapper;

    public CTypeServiceImpl(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    @Override
    public Result<?> addType(CTypeEntity cTypeEntity) {
        if (cTypeEntity.getType() == null) {
            return Result.failure("课程类别不能为空");
        }
        if (this.getOne(new QueryWrapper<CTypeEntity>().eq("type", cTypeEntity.getType())) != null) {
            return Result.failure("课程类别已存在");
        }
        this.save(cTypeEntity);
        return Result.success(cTypeEntity.getType() + "添加成功");
    }

    @Override
    public Result<?> updateType(CTypeEntity cTypeEntity) {
        if (cTypeEntity.getType() == null) {
            return Result.failure("课程类别不能为空");
        }
        if (this.getOne(new QueryWrapper<CTypeEntity>().eq("type", cTypeEntity.getType())) == null) {
            return Result.failure("课程类别不存在");
        }
        this.updateById(cTypeEntity);
        return Result.success(cTypeEntity.getType() + "修改成功");
    }

    @Override
    public Result<?> deleteType(Integer id) {
        if (this.getOne(new QueryWrapper<CTypeEntity>().eq("id", id)) == null) {
            return Result.failure("课程类别不存在");
        }
        CourseEntity courseEntity = courseMapper.selectOne(new QueryWrapper<CourseEntity>().eq("type", id));
        if (courseEntity != null) {
            return Result.failure("该课程类别下有课程" + courseEntity.getName() + "，无法删除");
        }
        this.removeById(id);
        return Result.success("删除成功");
    }

    @Override
    public Result<?> getType() {
        List<CTypeEntity> cTypeEntities = this.list(new QueryWrapper<CTypeEntity>().select("ID", "type"));

        List<Map<String, Object>> formattedList = new ArrayList<>();
        for (CTypeEntity cType : cTypeEntities) {
            Map<String, Object> map = new HashMap<>();
            map.put("value", cType.getId());
            map.put("label", cType.getType());
            formattedList.add(map);
        }

        return Result.success(formattedList);
    }


}
