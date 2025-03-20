/**
 * @Author: wangbo 3812943352@qq.com
 * @Date: 2024-11-13 18:49:31
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-13 08:56:06
 * @FilePath: user-service/src/main/java/org/learning/userservice/service/SmsService.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.userservice.service;

import com.common.commonmodule.resp.Result;

public interface SmsService {
    Result<?> sendSms(String phoneNumbers);

    Boolean validateSms(String captchaKey, String userInput);

}
