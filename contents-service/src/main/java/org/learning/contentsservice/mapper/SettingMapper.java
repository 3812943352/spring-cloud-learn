/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 15:53:58
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-14 15:59:23
 * @FilePath: contents-service/src/main/java/org/learning/contentsservice/mapper/SettingMapper.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.contentsservice.mapper;

import org.learning.contentsservice.entity.SettingEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 王博
 * @since 2025-03-14
 */
@Mapper
public interface SettingMapper extends BaseMapper<SettingEntity> {

}
