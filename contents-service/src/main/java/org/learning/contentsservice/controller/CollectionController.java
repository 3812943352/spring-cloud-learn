/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 09:02:37
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-26 13:50:57
 * @FilePath: contents-service/src/main/java/org/learning/contentsservice/controller/CollectionController.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.contentsservice.controller;

import com.common.commonmodule.resp.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.learning.contentsservice.entity.ACommentEntity;
import org.learning.contentsservice.entity.CollectionEntity;
import org.learning.contentsservice.service.CollectionService;
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
public class CollectionController {
    private final CollectionService collectionService;

    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @Operation(summary = "添加收藏")
    @PostMapping(value = "/addCollection")
    public Result<?> add(@RequestBody CollectionEntity collectionEntity) {
        return this.collectionService.add(collectionEntity);
    }

    @Operation(summary = "收藏分页")
    @PostMapping(value = "/getCollection")
    public Result<?> get(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        return this.collectionService.get(pageNum, pageSize);
    }


    @Operation(summary = "收藏模糊")
    @PostMapping(value = "/blurCollection")
    public Result<?> blur(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize,
                          @RequestParam("word") String word) {
        return this.collectionService.blur(pageNum, pageSize, word);
    }


    @Operation(summary = "收藏日期")
    @PostMapping(value = "/dateCollection")
    public Result<?> date(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                          @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                          @RequestParam("start") long startTime,
                          @RequestParam("end") long endTime) {
        return this.collectionService.date(pageNum, pageSize, startTime, endTime);
    }
}
