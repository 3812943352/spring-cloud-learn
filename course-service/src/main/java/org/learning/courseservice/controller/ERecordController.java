/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 16:01:19
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-08 04:25:04
 * @FilePath: course-service/src/main/java/org/learning/courseservice/controller/ERecordController.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.controller;

import com.common.commonmodule.resp.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.learning.courseservice.service.ERecordService;
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
 * @since 2025-03-14
 */
@RestController
@RequestMapping("/course")
public class ERecordController {
    private final ERecordService eRecordService;

    public ERecordController(ERecordService eRecordService) {
        this.eRecordService = eRecordService;
    }

    @Operation(summary = "证书列表")
    @PostMapping(value = "/getErecord")
    public Result<?> getErecord(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
        return this.eRecordService.get(pageNum, pageSize);
    }

    @Operation(summary = "模糊查询证书")
    @PostMapping(value = "/blurErecord")
    public Result<?> blurErecord(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                 @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                 @RequestParam("word") String word) {
        return this.eRecordService.blur(pageNum, pageSize, word);
    }

    @Operation(summary = "根据时间查询证书")
    @PostMapping(value = "/dateErecord")
    public Result<?> dateErecord(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                 @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                 @RequestParam("start") long startTime,
                                 @RequestParam("end") long endTime) {
        return this.eRecordService.date(pageNum, pageSize, startTime, endTime);
    }

    @Operation(summary = "根据时间查询证书")
    @PostMapping(value = "/userRecord")
    public Result<?> userRecord(
            @RequestParam("user") String user) {
        return this.eRecordService.userRecord(user);
    }
}
