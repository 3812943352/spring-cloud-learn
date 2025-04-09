/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-04-03 01:37:40
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-08 03:23:40
 * @FilePath: video-service/src/main/java/org/learning/videoservice/controller/ProgressController.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.videoservice.controller;

import com.common.commonmodule.resp.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.learning.videoservice.entity.ProgressEntity;
import org.learning.videoservice.service.ProgressService;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 王博
 * @since 2025-04-03
 */
@RestController
@RequestMapping("/video")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @Operation(summary = "新增或更新视频进度")
    @PostMapping(value = "/addOrUpdateProgress")
    public Result<?> addOrUpdateProgress(@RequestBody ProgressEntity progressEntity) {
        return this.progressService.addOrUpdateProgress(progressEntity);
    }

    @Operation(summary = "用户观看记录")
    @PostMapping(value = "/userVideoRecord")
    public Result<?> userVideoRecord(@RequestParam(value = "user") int user) {
        return this.progressService.userVideoRecord(user);
    }

    @Operation(summary = "视频进度")
    @PostMapping(value = "/videoProgress")
    public Result<?> videoProgress(@RequestParam(value = "user") int user, @RequestParam(value = "video") int video) {
        return this.progressService.videoProgress(user, video);
    }


    @Operation(summary = "是否学习完毕")
    @PostMapping(value = "/isDone")
    public Result<?> isDone(@RequestParam(value = "user") int user, @RequestParam(value = "course") int course) {
        return this.progressService.isDone(user, course);
    }

    @Operation(summary = "学习进度")
    @PostMapping(value = "/courseProgress")
    public Result<?> courseProgress(@RequestParam(value = "user") int user, @RequestParam(value = "course") int course) {
        return this.progressService.courseProgress(user, course);
    }
}
