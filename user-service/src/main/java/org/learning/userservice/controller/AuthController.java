/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 09:55:22
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-14 11:06:16
 * @FilePath: user-service/src/main/java/org/learning/userservice/controller/AuthController.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.userservice.controller;

import com.common.commonmodule.resp.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.learning.userservice.entity.AuthEntity;
import org.learning.userservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/user")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Operation(summary = "权限信息")
    @PostMapping(value = "/getAuth")
    public Result<?> getAuth(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        return authService.getAuth(pageNum, pageSize);
    }

    @Operation(summary = "添加权限")
    @PostMapping(value = "/addAuth")
    public Result<?> addAuth(@RequestBody AuthEntity authEntity) {
        return authService.addAuth(authEntity);
    }

    @Operation(summary = "删除权限")
    @PostMapping(value = "/delAuth")
    public Result<?> delAuth(@RequestBody AuthEntity authEntity) {
        return authService.delAuth(authEntity);
    }

    @Operation(summary = "更新权限")
    @PostMapping(value = "/updateAuth")
    public Result<?> updateAuth(@RequestBody AuthEntity authEntity) {
        return authService.updateAuth(authEntity);
    }
}
