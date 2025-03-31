/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 15:57:55
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-30 13:20:48
 * @FilePath: contents-service/src/main/java/org/learning/contentsservice/controller/SettingController.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.contentsservice.controller;

import cn.hutool.setting.Setting;
import com.common.commonmodule.resp.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.learning.contentsservice.service.SettingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 王博
 * @since 2025-03-14
 */
@RestController
@RequestMapping("/contents")
public class SettingController {

    private final SettingService settingService;

    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    @Operation(summary = "轮播图修改")
    @PostMapping(value = "/updateSetting")
    public Result<?> updateSetting(@RequestParam(value = "file1", required = false) MultipartFile file1,
                                   @RequestParam(value = "file2", required = false) MultipartFile file2,
                                   @RequestParam(value = "file3", required = false) MultipartFile file3,
                                   @RequestParam(value = "file4", required = false) MultipartFile file4,
                                   @RequestParam(value = "file5", required = false) MultipartFile file5,
                                   @RequestParam("phone") String phone,
                                   @RequestParam("email") String email,
                                   @RequestParam("num") String num,
                                   @RequestParam("cr") String cr

    ) {
        return this.settingService.updateSetting(file1, file2, file3, file4, file5, phone, email, num, cr);
    }

    @Operation(summary = "轮播图src获取")
    @PostMapping(value = "/getSetting")
    public Result<?> getSetting() {
        return this.settingService.get(1);
    }
}
