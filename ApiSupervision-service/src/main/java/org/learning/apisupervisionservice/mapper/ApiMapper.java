/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-12 16:53:42
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-13 16:25:46
 * @FilePath: ApiSupervision-service/src/main/java/org/learning/apisupervisionservice/mapper/ApiMapper.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.apisupervisionservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.learning.apisupervisionservice.entity.ApiEntity;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 王博
 * @since 2025-03-12
 */
@Mapper
public interface ApiMapper extends BaseMapper<ApiEntity> {
    List<String> getAllApi();

}
