/**
 * @Author: wangbo 3812943352@qq.com
 * @Date: 2024-11-11 14:22:10
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-12 21:02:46
 * @FilePath: gateway-service/src/main/java/org/learning/gatewayservice/mapper/ApiMapper.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.gatewayservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.learning.gatewayservice.entity.ApiEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 王博
 * @since 2024-11-09
 */
@Mapper
public interface ApiMapper extends BaseMapper<ApiEntity> {
    List<String> getAllApi();
}
