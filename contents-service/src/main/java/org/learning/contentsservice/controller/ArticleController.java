/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 09:01:55
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-27 13:35:49
 * @FilePath: contents-service/src/main/java/org/learning/contentsservice/controller/ArticleController.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.contentsservice.controller;

import com.common.commonmodule.resp.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.learning.contentsservice.entity.ArticleEntity;
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
public class ArticleController {


    private final ArticleService articleService;

    public ArticleController(ArticleService articleService, ArticleService articleService1) {
        this.articleService = articleService1;
    }

    @Operation(summary = "添加文章")
    @PostMapping(value = "/addArticle")
    public Result<?> add(@ModelAttribute ArticleEntity articleEntity,
                         @RequestParam(value = "file1", required = false) MultipartFile file1,
                         @RequestParam(value = "file2", required = false) MultipartFile file2,
                         @RequestParam(value = "file3", required = false) MultipartFile file3,
                         @RequestParam(value = "file4", required = false) MultipartFile file4,
                         @RequestParam(value = "file5", required = false) MultipartFile file5) {
        return this.articleService.addArticle(file1, file2, file3, file4, file5, articleEntity);
    }

    @Operation(summary = "删除文章")
    @PostMapping(value = "/delArticle")
    public Result<?> del(@RequestParam("id") int id) {
        return this.articleService.del(id);
    }

    @Operation(summary = "更新文章")
    @PostMapping(value = "/updateArticle")
    public Result<?> update(ArticleEntity articleEntity,
                            @RequestParam(value = "file1", required = false) MultipartFile file1,
                            @RequestParam(value = "file2", required = false) MultipartFile file2,
                            @RequestParam(value = "file3", required = false) MultipartFile file3,
                            @RequestParam(value = "file4", required = false) MultipartFile file4,
                            @RequestParam(value = "file5", required = false) MultipartFile file5
    ) {
        return this.articleService.update(file1, file2, file3, file4, file5, articleEntity);

    }


    @Operation(summary = "文章分页")
    @PostMapping(value = "/getArticle")
    public Result<?> getArticle(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        return this.articleService.getArticle(pageNum, pageSize);
    }


    @Operation(summary = "文章模糊")
    @PostMapping(value = "/blurArticle")
    public Result<?> blurArticle(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize,
                                 @RequestParam("word") String word) {
        return this.articleService.blur(pageNum, pageSize, word);
    }


    @Operation(summary = "文章日期")
    @PostMapping(value = "/dateArticle")
    public Result<?> dateArticle(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                 @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                 @RequestParam("start") long startTime,
                                 @RequestParam("end") long endTime) {
        return this.articleService.date(pageNum, pageSize, startTime, endTime);
    }

    @Operation(summary = "文章类型列表")
    @PostMapping(value = "/listArticle")
    public Result<?> listArticle(int type) {
        return this.articleService.listArticle(type);
    }
}
