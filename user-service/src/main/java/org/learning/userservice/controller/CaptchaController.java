/**
 * @Author: wangbo 3812943352@qq.com
 * @Date: 2024-11-12 17:05:34
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-13 08:57:49
 * @FilePath: user-service/src/main/java/org/learning/userservice/controller/CaptchaController.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.userservice.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/user")
@Tag(name = "Captcha", description = "验证码相关接口")
public class CaptchaController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${config.redisTimeout}")
    private Long redisTimeout;

    @Operation(summary = "生成图形验证码", description = "生成一个新的图形验证码并返回图片")
    @GetMapping(value = "/captcha", produces = MediaType.IMAGE_PNG_VALUE)
    public void generateCaptcha(HttpServletResponse response, WebRequest request) throws IOException {
        // 定义图形验证码的长宽
        int width = 76;
        int height = 40;
        // 定义验证码字符数
        int codeCount = 4;
        // 定义干扰元素个数
        int lineCount = 50;

        // 创建LineCaptcha对象
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(width, height, codeCount, lineCount);

        // 获取验证码文本
        String code = lineCaptcha.getCode();

        // 生成唯一标识符作为键
        String key = UUID.randomUUID().toString();
        // 将验证码保存到Redis中，设置过期时间
        redisTemplate.opsForValue().set(key, code, redisTimeout * 3, TimeUnit.SECONDS);

        // 将键保存到session或cookie中，以便后续验证
        // 输出验证码图片
        lineCaptcha.write(response.getOutputStream());
        response.setHeader("captcha-key", key);
    }
}