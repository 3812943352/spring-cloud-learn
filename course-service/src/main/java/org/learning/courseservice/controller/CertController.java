/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-08 07:26:25
 * @FilePath: course-service/src/main/java/org/learning/courseservice/controller/CertController.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.controller;

import com.common.commonmodule.resp.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.learning.courseservice.service.CertService;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 王博
 * @since 2025-03-13
 */
@RestController
@RequestMapping("/course")
public class CertController {
    private final CertService certService;

    public CertController(CertService certService) {
        this.certService = certService;
    }

    @Operation(summary = "证书列表")
    @PostMapping(value = "/getCert")
    public Result<?> getCert(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                             @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
        return this.certService.get(pageNum, pageSize);
    }

    @Operation(summary = "模糊查询证书")
    @PostMapping(value = "/blurCert")
    public Result<?> blurCert(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                              @RequestParam("word") String word) {
        return this.certService.blur(pageNum, pageSize, word);
    }

    @Operation(summary = "根据时间查询证书")
    @PostMapping(value = "/dateCert")
    public Result<?> dateCert(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                              @RequestParam("start") long startTime,
                              @RequestParam("end") long endTime) {
        return this.certService.date(pageNum, pageSize, startTime, endTime);
    }


    @Operation(summary = "用户手机号查询证书")
    @PostMapping(value = "/userCert")
    public Result<?> userCert(@RequestParam(value = "phone") String phone) {
        return this.certService.userCert(phone);
    }

    @Operation(summary = "证书验证接口")
    @PostMapping(value = "/certValid")
    public Result<?> certValid(@RequestParam(value = "idNum") String idNum,
                               @RequestParam(value = "phone") String phone,
                               @RequestParam(value = "cert") String cert,
                               @RequestParam(value = "cap") String userInput,
                               @RequestHeader("captcha-key") String captchaKey
    ) {
        Result<?> captchaValidationResult = this.validateCaptcha(captchaKey, userInput);
        if (captchaValidationResult != null) {
            return captchaValidationResult;
        }
        return this.certService.certValid(idNum, phone, cert);
    }

    private Result<?> validateCaptcha(String captchaKey, String userInput) {
        if (!this.certService.validateCaptcha(captchaKey, userInput)) {
            return Result.failure(203, "验证码错误或已过期");
        }
        return null;
    }
}
