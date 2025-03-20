package org.learning.courseservice.mapper; /**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-16 11:47:48
 * @FilePath: course-service/src/main/java/org/learning/courseservice/mapper/CourseMapper.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.learning.courseservice.entity.CourseEntity;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 王博
 * @since 2025-03-13
 */
@Mapper
//@Repository
public interface CourseMapper extends BaseMapper<CourseEntity> {


}