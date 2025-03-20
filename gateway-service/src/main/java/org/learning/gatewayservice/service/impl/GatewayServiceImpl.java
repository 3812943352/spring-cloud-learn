/**
 * @Author: wangbo 3812943352@qq.com
 * @Date: 2024-11-17 13:01:26
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-12 21:03:34
 * @FilePath: gateway-service/src/main/java/org/learning/gatewayservice/service/impl/GatewayServiceImpl.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.gatewayservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.learning.gatewayservice.entity.GatewayEntity;
import org.learning.gatewayservice.mapper.GatewayMapper;
import org.learning.gatewayservice.service.GatewayService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 王博
 * @since 2024-11-17
 */
@Service
public class GatewayServiceImpl extends ServiceImpl<GatewayMapper, GatewayEntity> implements GatewayService {

    @Override
    public void info(String username, String reqAdd, String reqMethod,
                     String reqIp, String ua, String reqController,
                     String reqName, String reqCode, String error,
                     String contentType, Long contentLength, String uri,
                     String jwt, TemporalAccessor reqTime, TemporalAccessor resTime, Duration ms
    ) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault())
                .withLocale(Locale.getDefault());
        String req_time = formatter.format(reqTime);
        String res_time = formatter.format(resTime);
        Long time = ms.toMillis();
        GatewayEntity ge = new GatewayEntity();
        ge.setUsername(username);
        ge.setReqAdd(reqAdd);
        ge.setReqMethod(reqMethod);
        ge.setReqIp(reqIp);
        ge.setUa(ua);
        ge.setReqController(reqController);
        ge.setReqName(reqName);
        ge.setReqCode(reqCode);
        ge.setErrorinfo(error);
        ge.setContentType(contentType);
        ge.setContentLength(contentLength);
        ge.setUri(uri);
        ge.setJwt(jwt);
        ge.setReqTime(convertToTimestamp(req_time) / 1000);
        ge.setResTime(convertToTimestamp(res_time) / 1000);
        ge.setMs(time);
        this.save(ge);

    }

    private long convertToTimestamp(String dateTimeString) {
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 将字符串解析为 LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);

        // 将 LocalDateTime 转换为 Instant
        // 注意：这里假设系统默认时区是正确的
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();

        // 获取 Unix 时间戳（毫秒）
        return instant.toEpochMilli();
    }

}
