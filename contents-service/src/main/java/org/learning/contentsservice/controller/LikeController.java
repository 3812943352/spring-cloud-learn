/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 09:03:26
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-26 13:51:46
 * @FilePath: contents-service/src/main/java/org/learning/contentsservice/controller/LikeController.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.contentsservice.controller;

import com.common.commonmodule.resp.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.learning.contentsservice.entity.ACommentEntity;
import org.learning.contentsservice.entity.LikeEntity;
import org.learning.contentsservice.service.LikeService;
import org.springframework.web.bind.annotation.*;

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
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @Operation(summary = "添加评论")
    @PostMapping(value = "/addLike")
    public Result<?> add(@RequestBody LikeEntity likeEntity) {
        return this.likeService.add(likeEntity);
    }

    @Operation(summary = "文章分页")
    @PostMapping(value = "/getLike")
    public Result<?> get(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        return this.likeService.get(pageNum, pageSize);
    }


    @Operation(summary = "文章模糊")
    @PostMapping(value = "/blurLike")
    public Result<?> blur(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize,
                          @RequestParam("word") String word) {
        return this.likeService.blur(pageNum, pageSize, word);
    }


    @Operation(summary = "文章日期")
    @PostMapping(value = "/dateLike")
    public Result<?> date(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                          @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                          @RequestParam("start") long startTime,
                          @RequestParam("end") long endTime) {
        return this.likeService.date(pageNum, pageSize, startTime, endTime);
    }
}
