/**
 * @Author: wangbo 3812943352@qq.com
 * @Date: 2024-11-09 09:45:34
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-06 14:29:16
 * @FilePath: gateway-service/src/main/java/org/learning/gatewayservice/config/GlobalFilterConfig.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.gatewayservice.config;

import org.learning.gatewayservice.service.ApiService;
import org.learning.gatewayservice.service.GatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RefreshScope
public class GlobalFilterConfig implements GlobalFilter, Ordered {
    private static final String HEADER_NAME = "Token";
    private final ApiService apiService;
    //application.yml配置文件中，设置token在redis中的过期时间
    @Value("${config.redisTimeout}")
    private Long redisTimeout;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private GatewayService gatewayService;


    @Autowired
    public GlobalFilterConfig(ApiService apiService) {
        this.apiService = apiService;
    }

    public static String getIpAddress(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().getAddress().getHostAddress();
        }
        return ip;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("============过滤器============");
        // 获取请求对象
        ServerHttpRequest request = exchange.getRequest();
        // 获取响应对象
        ServerHttpResponse response = exchange.getResponse();
        String reqController = request.getURI().getPath();

        String url = request.getURI().getPath();
        // 获取token信息
        String token = request.getHeaders().getFirst(HEADER_NAME);
        List<String> apiList = this.apiService.getApi();
        String contentType = getContentType(exchange);
        Long contentLength = getContentLength(exchange);
        String userAgent = getUserAgent(exchange);
        String method = request.getMethod().toString();
        Boolean isApi = apiList.contains(reqController);
        String reqAdd = request.getURI().toString();
        String reqCode = response.getStatusCode() != null ? String.valueOf(response.getStatusCode().value()) : "未知";
        String uri = request.getURI().toString();
        // 记录请求开始时间戳
        String ip = getIpAddress(request);
        Instant startTime = Instant.now();
//        System.out.println("ip:" + ip);
//        System.out.println("请求接口" + request.getURI().getPath());
//        System.out.println("请求方法" + request.getMethod());
//        System.out.println("uri" + request.getURI());
//        System.out.println("code" + response.getStatusCode());
//        System.out.println(this.apiService.getApi());

        if (isApi) {
            this.apiService.addVisit(reqController);

        }
        // 获取请求地址
        if (this.redisTemplate.hasKey(ip)) {
            response.setStatusCode(HttpStatus.OK);
            response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            byte[] bytes = "{\"message\": \"您的IP已被封禁\",\"code\": 444}".getBytes();
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));
        }


        // 判断是否为白名单请求，以及一些内置不需要验证的请求。(登录请求也包含其中)。
        // 如果当前请求中包含token令牌不为空的时候，也会继续验证Token的合法性，这样就能保证
        // Token中的用户信息被业务接口正常访问到了。而如果当token为空的时候，白名单的接口可以
        // 被网关直接转发，无需登录验证。当然被转发的接口，也无法获取到用户的token身份数据了。

        if (this.apiService.apiShouldNot(url)) {
            String Id = "未登录";
            String jwt = "未登录";
            if (isApi) {
                this.apiService.addTimes(reqController);
            }
            return chain.filter(exchange)
                    .doOnSuccess(aVoid -> {
                        String error = "无";
                        Instant endTime = Instant.now();
                        Duration duration = Duration.between(startTime, endTime);
                        gatewayService.info(Id, reqAdd, method, ip, userAgent,
                                reqController, reqController, reqCode, error, contentType,
                                contentLength, uri, jwt, startTime, endTime, duration);
                    })
                    .doOnError(throwable -> {
                        String error = throwable.getMessage();
                        Instant endTime = Instant.now();
                        Duration duration = Duration.between(startTime, endTime);
                        this.apiService.addError(reqController);
                        gatewayService.info(Id, reqAdd, method, ip, userAgent,
                                reqController, reqController, reqCode, error, contentType,
                                contentLength, uri, jwt, startTime, endTime, duration);
                    });
        }
        if (StringUtils.isEmpty(token)) {
            return this.unAuthorize(exchange);
        }
        if (!this.redisTemplate.hasKey(token)) {
            return this.unAuthorize(exchange);
        }
        System.out.println(this.apiService.apiUserList(url, token));
        System.out.println(url);
        if (!this.apiService.apiUserList(url, token)) {
            response.setStatusCode(HttpStatus.OK);
            response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            byte[] bytes = "{\"message\": \"您无权访问该接口\",\"code\": 443}".getBytes();
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));
        }

        // 验证通过，刷新token过期时间
        this.redisTemplate.expire(token, this.redisTimeout, TimeUnit.HOURS);
        String userId = String.valueOf(this.redisTemplate.opsForValue().get(token));
        System.out.println("============登录用户id：" + userId + "============");
        // 把新的 exchange放回到过滤链
        ServerHttpRequest newRequest = request.mutate().header(HEADER_NAME, token).build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
        String jwt = "已校验";
        if (isApi) {
            this.apiService.addTimes(reqController);
        }
        // 使用doOnSuccess记录响应完成时间戳
        return chain.filter(newExchange)
                .doOnSuccess(aVoid -> {
                    Instant endTime = Instant.now();
                    Duration duration = Duration.between(startTime, endTime);
                    String error = "无";
                    gatewayService.info(userId, reqAdd, method, ip, userAgent,
                            reqController, reqController, reqCode, error, contentType,
                            contentLength, uri, jwt, startTime, endTime, duration);

                })
                .doOnError(throwable -> {
                    Instant endTime = Instant.now();
                    Duration duration = Duration.between(startTime, endTime);
                    String error = throwable.getMessage();
                    this.apiService.addError(reqController);
                    gatewayService.info(userId, reqAdd, method, ip, userAgent,
                            reqController, reqController, reqCode, error, contentType,
                            contentLength, uri, jwt, startTime, endTime, duration);
                });
    }

    @Override
    public int getOrder() {
        return 0;
    }

    // 返回未登录的自定义错误
    private Mono<Void> unAuthorize(ServerWebExchange exchange) {
        // 设置错误状态码为401
        exchange.getResponse().setStatusCode(HttpStatus.OK);
        // 设置返回的信息为JSON类型
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        // 自定义错误信息
        String errorMsg = "{\"error\": \"" + "用户未登录或登录超时,请重新登录" + "\",\"code\": 440}";
        // 将自定义错误响应写入响应体
        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(errorMsg.getBytes())));
    }


    private String getContentType(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
    }

    private Long getContentLength(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().getContentLength();
    }

    private String getUserAgent(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().getFirst(HttpHeaders.USER_AGENT);
    }

}