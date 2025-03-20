/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 14:15:33
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-13 14:59:41
 * @FilePath: ApiSupervision-service/src/main/java/org/learning/apisupervisionservice/controller/IpController.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.apisupervisionservice.controller;

import com.common.commonmodule.resp.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.learning.apisupervisionservice.service.IpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 王博
 * @since 2025-03-13
 */
@RestController
@RequestMapping("/ApiSuperVision")
@Tag(name = "IpController", description = "IP管理")
public class IpController {

    private final IpService ip;

    @Autowired
    private IpController(IpService ip) {
        this.ip = ip;
    }

    @Operation(summary = "封禁列表")
    @PostMapping(value = "/getBan")
    public Result<?> getBan(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        return this.ip.getBan(pageNum, pageSize);
    }

    @Operation(summary = "封禁IP")
    @PostMapping(value = "/ban")
    public Result<?> ban(@RequestParam("ip") String ip, @RequestParam("reason") String reason, @RequestParam("time") Long time) {
        return this.ip.setBan(ip, reason, time);
    }

    @Operation(summary = "解封IP")
    @PostMapping(value = "/unBan")
    public Result<?> unBan(@RequestParam("ip") String ip) {
        return this.ip.unBan(ip);
    }

    @Operation(summary = "模糊搜索")
    @PostMapping(value = "/banBlur")
    public Result<?> getBlur(@RequestParam("pageNum") int pageNum,
                             @RequestParam("pageSize") int pageSize,
                             @RequestParam("word") String word
    ) {
        return this.ip.getBlur(pageNum, pageSize, word);
    }

    @Operation(summary = "日期搜索")
    @PostMapping(value = "/banDate")
    public Result<?> getDate(@RequestParam("pageNum") int pageNum,
                             @RequestParam("pageSize") int pageSize,
                             @RequestParam("start") long start,
                             @RequestParam("end") long end
    ) {
        return this.ip.getDate(pageNum, pageSize, start, end);
    }

}