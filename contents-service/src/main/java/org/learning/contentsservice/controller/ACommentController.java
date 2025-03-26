/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 09:01:17
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-26 10:41:00
 * @FilePath: contents-service/src/main/java/org/learning/contentsservice/controller/ACommentController.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.contentsservice.controller;

import com.common.commonmodule.resp.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.learning.contentsservice.entity.ACommentEntity;
import org.learning.contentsservice.entity.ArticleEntity;
import org.learning.contentsservice.service.ACommentService;
import org.learning.contentsservice.service.ArticleService;
import org.springframework.web.bind.annotation.*;
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
public class ACommentController {

    private final ACommentService aCommentService;

    public ACommentController(ACommentService aCommentService) {
        this.aCommentService = aCommentService;
    }

    @Operation(summary = "添加评论")
    @PostMapping(value = "/addComment")
    public Result<?> add(@RequestBody ACommentEntity aCommentEntity) {
        return this.aCommentService.add(aCommentEntity);
    }

    @Operation(summary = "删除评论")
    @PostMapping(value = "/delComment")
    public Result<?> del(@RequestParam("id") int id) {
        return this.aCommentService.del(id);
    }

    @Operation(summary = "更新评论")
    @PostMapping(value = "/updateComment")
    public Result<?> update(@RequestBody ACommentEntity aCommentEntity) {
        return this.aCommentService.update(aCommentEntity);
    }

    @Operation(summary = "文章分页")
    @PostMapping(value = "/getComment")
    public Result<?> get(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        return this.aCommentService.get(pageNum, pageSize);
    }


    @Operation(summary = "文章模糊")
    @PostMapping(value = "/blurComment")
    public Result<?> blur(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize,
                          @RequestParam("word") String word) {
        return this.aCommentService.blur(pageNum, pageSize, word);
    }


    @Operation(summary = "文章日期")
    @PostMapping(value = "/dateComment")
    public Result<?> date(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                          @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                          @RequestParam("start") long startTime,
                          @RequestParam("end") long endTime) {
        return this.aCommentService.date(pageNum, pageSize, startTime, endTime);
    }
}
