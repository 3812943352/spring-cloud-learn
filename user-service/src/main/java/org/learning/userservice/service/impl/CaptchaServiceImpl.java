/**
 * @Author: wangbo 3812943352@qq.com
 * @Date: 2024-11-12 20:00:05
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-13 08:54:52
 * @FilePath: user-service/src/main/java/org/learning/userservice/service/impl/CaptchaServiceImpl.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.userservice.service.impl;

import org.learning.userservice.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CaptchaServiceImpl implements CaptchaService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Boolean validateCaptcha(String captchaKey, String userInput) {
        // 从Redis中获取验证码
        String storedCaptcha = redisTemplate.opsForValue().get(captchaKey);

        // 验证用户输入的验证码是否与Redis中的验证码一致
        if (storedCaptcha != null && storedCaptcha.equalsIgnoreCase(userInput)) {
            // 验证成功后删除Redis中的验证码
            redisTemplate.delete(captchaKey);
            return true;
        } else {
            return false;
        }
    }
}
