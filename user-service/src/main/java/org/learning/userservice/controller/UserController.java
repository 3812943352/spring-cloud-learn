/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 09:50:28
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-13 10:00:18
 * @FilePath: user-service/src/main/java/org/learning/userservice/controller/UserController.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.userservice.controller;

import com.common.commonmodule.Valid.ValidPassword;
import com.common.commonmodule.Valid.ValidPhone;
import com.common.commonmodule.resp.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.learning.userservice.entity.UserEntity;
import org.learning.userservice.service.CaptchaService;
import org.learning.userservice.service.SmsService;
import org.learning.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 王博
 * @since 2025-03-13
 */
@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "用户相关接口")
@Validated
public class UserController {
    private final UserService userService;
    private final SmsService smsService;

    private final CaptchaService captchaService;

    @Autowired
    public UserController(UserService userService, SmsService smsService, CaptchaService captchaService) {
        this.userService = userService;
        this.smsService = smsService;
        this.captchaService = captchaService;
    }

    @Operation(summary = "获取数据库信息")
    @PostMapping(value = "/getDatabase")
    public Result<?> getAllDatabase() {
        return userService.Database();
    }

    @Operation(summary = "用户登录")
    @PostMapping(value = "/login")
    public Result<?> login(@Valid @RequestParam("phone") @ValidPhone() String phone,
                           @RequestParam("smsCode") String userInput
    ) {
        // 验证手机验证码
        Result<?> smsValidationResult = this.validateSms(phone, userInput);
        if (smsValidationResult != null) {
            return smsValidationResult;
        }
        // 调用服务层的方法进行登录逻辑处理
        return this.userService.login(phone);
    }

    @Operation(summary = "用户注册")
    @PostMapping(value = "/register")
    public Result<?> register(@Valid @RequestBody UserEntity userEntity, BindingResult bindingResult,
                              @RequestHeader("captcha-Key") String captchaKey,
                              @RequestParam("captcha") String userInput
    ) {
        Result<?> validationResult = this.validate(bindingResult);
        if (validationResult != null) {
            return validationResult;
        }
        Result<?> captchaValidationResult = this.validateCaptcha(captchaKey, userInput);
        if (captchaValidationResult != null) {
            return captchaValidationResult;
        }
        // 调用服务层的方法进行注册逻辑处理
        return this.userService.register(userEntity);
    }

    @Operation(summary = "修改密码")
    @PostMapping(value = "/reset")
    public Result<?> reset(@Valid @RequestParam("phone") @ValidPhone String phone,
                           @Valid @RequestParam("pwd") @ValidPassword() String pwd,
                           @RequestParam("smsCode") String userInput
    ) {
        Result<?> smsValidationResult = this.validateSms(phone, userInput);
        if (smsValidationResult != null) {
            return smsValidationResult;
        }
        // 调用服务层的方法进行注册逻辑处理
        return this.userService.resetPwd(phone, pwd);
    }

    @Operation(summary = "模糊搜索")
    @PostMapping(value = "/userBlur")
    public Result<?> getBlur(@RequestParam("pageNum") int pageNum,
                             @RequestParam("pageSize") int pageSize,
                             @RequestParam("word") String word
    ) {
        return userService.userBlur(pageNum, pageSize, word);
    }

    @Operation(summary = "ID查找用户")
    @PostMapping(value = "/getUserById")
    public Result<?> getUserById(@RequestParam("id") int id) {
        return Result.success(this.userService.getUserById(id));
    }

    @Operation(summary = "手机号查找用户")
    @PostMapping(value = "/getUserByPhone")
    public Result<?> getUserById(@RequestParam("phone") String phone) {
        return Result.success(this.userService.getUserByPhone(phone));
    }

    @Operation(summary = "分页获取所有用户")
    @PostMapping(value = "/getAllUser")
    public Result<?> getAllUser(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        return Result.success(userService.getAllUserPagination(pageNum, pageSize));
    }

    @Operation(summary = "根据ID删除用户")
    @PostMapping(value = "/delUser")
    public Result<?> delUserById(@Valid @RequestBody UserEntity userEntity, BindingResult bindingResult) {
        Result<?> validationResult = this.validate(bindingResult);
        if (validationResult != null) {
            return validationResult;
        }
        return this.userService.DelUserById(userEntity);
    }

    @Operation(summary = "根据ID更改用户信息")
    @PostMapping(value = "/updateUserById")
    public Result<?> updateUserById(@RequestBody UserEntity userEntity) {

        return this.userService.updateUser(userEntity);
    }

    private Result<?> validate(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (!fieldErrors.isEmpty()) {
            //取出所有校验不通过的信息
            List<String> collect = fieldErrors.stream().map(s -> s.getDefaultMessage()).collect(Collectors.toList());
            return Result.failure(202, collect);
        }
        return null;
    }

    private Result<?> validateSms(String phone, String userInput_sms) {
        if (!this.smsService.validateSms(phone, userInput_sms)) {
            return Result.failure(203, "验证码错误或已过期");
        }
        return null;
    }

    private Result<?> validateCaptcha(String captchaKey, String userInput) {
        if (!this.captchaService.validateCaptcha(captchaKey, userInput)) {
            return Result.failure(203, "验证码错误或已过期");
        }
        return null;
    }
}
