/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 08:55:02
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-13 08:55:17
 * @FilePath: user-service/src/main/java/org/learning/userservice/service/CaptchaService.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.userservice.service;

public interface CaptchaService {
    Boolean validateCaptcha(String captchaKey, String userInput);
}
