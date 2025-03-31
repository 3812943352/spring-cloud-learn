/**
 * @Author: wangbo 3812943352@qq.com
 * @Date: 2024-11-13 18:49:55
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-30 19:50:13
 * @FilePath: user-service/src/main/java/org/learning/userservice/controller/SmsController.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.userservice.controller;

import com.common.commonmodule.Valid.ValidPassword;
import com.common.commonmodule.Valid.ValidPhone;
import com.common.commonmodule.resp.Result;
import org.learning.userservice.entity.UserEntity;
import org.learning.userservice.service.CaptchaService;
import org.learning.userservice.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.learning.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "Sms", description = "短信接口")
@Validated
public class SmsController {
    private final SmsService smsService;
    private final CaptchaService captchaService;

    private final UserService userService;

    @Autowired
    public SmsController(SmsService smsService, CaptchaService captchaService, UserService userService) {
        this.smsService = smsService;
        this.captchaService = captchaService;
        this.userService = userService;

    }

    @Operation(summary = "发送短信")
    @PostMapping(value = "/sendSms")
    public Result<?> sendSms(@Valid @RequestParam("phone") @ValidPhone String phone,
                             @Valid @RequestParam("pwd") @ValidPassword String pwd,
                             @RequestHeader("captcha-key") String captchaKey,
                             @RequestParam("captcha") String userInput
    ) {
        UserEntity user = this.userService.getUserByPhone(phone);
        if (user == null) {
            return this.smsService.sendSms(phone);
        }
        String outh = user.getAuth();
        if (!outh.equals("管理")) {
            return Result.failure(201, "发送失败，请检查手机号或权限");
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (bCryptPasswordEncoder.matches(pwd, user.getPwd())) {
            Result<?> captchaValidationResult = this.validateCaptcha(captchaKey, userInput);
            if (captchaValidationResult != null) {
                return captchaValidationResult;
            }
            return this.smsService.sendSms(phone);
        } else {
            return Result.failure(201, "发送失败，请检查手机号或密码");
        }
    }

    @Operation(summary = "发送短信")
    @PostMapping(value = "resetSms")
    public Result<?> resetSms(@Valid @RequestParam("phone") @ValidPhone String phone,
                              @Valid @RequestParam("pwd") @ValidPassword String pwd,
                              @RequestHeader("captcha-key") String captchaKey,
                              @RequestParam("captcha") String userInput
    ) {
        UserEntity user = this.userService.getUserByPhone(phone);
        if (user != null) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            if (bCryptPasswordEncoder.matches(pwd, user.getPwd())) {
                return Result.failure(201, "请勿设置与旧密码一致的新密码");
            } else {
                Result<?> captchaValidationResult = this.validateCaptcha(captchaKey, userInput);
                if (captchaValidationResult != null) {
                    return captchaValidationResult;
                }
                return this.smsService.sendSms(phone);
            }

        } else {
            return Result.failure("手机号不存在,请检查手机号");
        }
    }

    private Result<?> validateCaptcha(String captchaKey, String userInput) {
        if (!this.captchaService.validateCaptcha(captchaKey, userInput)) {
            return Result.failure(203, "验证码错误或已过期");
        }
        return null;
    }


}
