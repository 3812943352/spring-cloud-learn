/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 09:55:36
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-13 09:57:14
 * @FilePath: user-service/src/main/java/org/learning/userservice/mapper/AuthMapper.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.learning.userservice.entity.AuthEntity;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 王博
 * @since 2025-03-13
 */
@Mapper
public interface AuthMapper extends BaseMapper<AuthEntity> {

}
