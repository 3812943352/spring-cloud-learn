package org.learning.courseservice.mapper; /**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-14 15:20:51
 * @FilePath: course-service/src/main/java/org/learning/courseservice/mapper/CertMapper.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.learning.courseservice.entity.CertEntity;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 王博
 * @since 2025-03-14
 */
@Mapper
public interface CertMapper extends BaseMapper<CertEntity> {

}