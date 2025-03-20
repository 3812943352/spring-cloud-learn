/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-12 16:56:13
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-12 17:23:28
 * @FilePath: gateway-service/src/main/java/org/learning/gatewayservice/service/GatewayService.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.gatewayservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.learning.gatewayservice.entity.GatewayEntity;

import java.time.Duration;
import java.time.temporal.TemporalAccessor;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 王博
 * @since 2025-03-12
 */
public interface GatewayService extends IService<GatewayEntity> {
    void info(String username, String reqAdd, String reqMethod,
              String reqIp, String ua, String reqController,
              String reqName, String reqCode, String error,
              String contentType, Long contentLength, String uri,
              String jwt, TemporalAccessor reqTime, TemporalAccessor resTime,
              Duration ms
    );
}
