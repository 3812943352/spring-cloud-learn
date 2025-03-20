/**
 * @Author: wangbo 3812943352@qq.com
 * @Date: 2024-11-13 18:49:07
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-13 08:56:06
 * @FilePath: user-service/src/main/java/org/learning/userservice/service/impl/SmsServiceImpl.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.userservice.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.common.commonmodule.resp.Result;
import org.learning.userservice.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
public class SmsServiceImpl implements SmsService {

    @Value("${config.sms.regionId}")
    private String regionId;

    @Value("${config.sms.accessKeyId}")
    private String accessKeyId;

    @Value("${config.sms.secret}")
    private String secret;

    @Value("${config.sms.signName}")
    private String signName;

    @Value("${config.sms.templateCode}")
    private String templateCode;

    @Value("${config.sms.smsTimeOut}")
    private int smsTimeout;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Result<?> sendSms(String phoneNumbers) {

        SecureRandom random = new SecureRandom();
        // 生成一个四位数的验证码，范围是[1000, 9999]
        int code = 1000 + random.nextInt(9000);
        //发送短信
        return this.sendMessage(phoneNumbers, String.valueOf(code));
    }

    @Override
    public Boolean validateSms(String phone, String userInput_sms) {
        // 从Redis中获取验证码
        String storedPhone = this.redisTemplate.opsForValue().get(phone);

        // 验证用户输入的验证码是否与Redis中的验证码一致
        // 验证成功后删除Redis中的验证码
        if (storedPhone != null && storedPhone.equalsIgnoreCase(userInput_sms)) {
            // 验证成功后删除Redis中的验证码
            redisTemplate.delete(phone);
            return true;
        }

        // 验证失败
        return false;
    }

    public Result<?> sendMessage(String phoneNumbers, String code) {

        if (!this.checkIfKeyExists(phoneNumbers)) {
            DefaultProfile profile = DefaultProfile.getProfile(this.regionId, this.accessKeyId, this.secret);
            IAcsClient client = new DefaultAcsClient(profile);

            SendSmsRequest request = new SendSmsRequest();
            request.setSysRegionId("cn-hangzhou");
            request.setPhoneNumbers(phoneNumbers);
            request.setSignName(this.signName);
            request.setTemplateCode(this.templateCode);
            request.setTemplateParam("{\"code\":\"" + code + "\"}");

            try {
                SendSmsResponse response = client.getAcsResponse(request);

                // 判断是否发送成功
                if ("OK".equals(response.getCode())) {
                    // 发送成功
                    this.redisTemplate.opsForValue().set(phoneNumbers, code, this.smsTimeout, TimeUnit.SECONDS);

                    System.out.println("短信发送成功: " + response);
                    return Result.success("短信验证码发送成功");
                } else {
                    // 发送失败
                    System.out.println("短信发送失败: " + response.getMessage());
                    return Result.failure(202, "短信发送失败: " + response.getMessage());
                }
            } catch (ClientException e) {
                // 异常处理
                String errMsg = e.getErrMsg();
                System.err.println("发送短信时发生错误: " + errMsg);
                return Result.failure(202, "发送短信时发生错误: " + errMsg);
            }
        }
        return Result.failure(202, "短信发送失败，请勿重复发送");
    }

    public boolean checkIfKeyExists(String key) {

        return Boolean.TRUE.equals(this.redisTemplate.hasKey(key));
    }
}
