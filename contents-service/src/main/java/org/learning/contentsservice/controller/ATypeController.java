/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 09:01:36
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-24 16:19:11
 * @FilePath: contents-service/src/main/java/org/learning/contentsservice/controller/ATypeController.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.contentsservice.controller;

import com.common.commonmodule.resp.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.learning.contentsservice.entity.ATypeEntity;
import org.learning.contentsservice.service.ATypeService;
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
public class ATypeController {
    private final ATypeService aTypeService;

    public ATypeController(@RequestBody ATypeService aTypeService) {
        this.aTypeService = aTypeService;
    }

    @Operation(summary = "创建文章类别")
    @PostMapping(value = "/addType")
    public Result<?> addType(@RequestBody ATypeEntity aTypeEntity) {
        return aTypeService.addType(aTypeEntity);
    }

    @Operation(summary = "更新文章类别")
    @PostMapping(value = "/updateType")
    public Result<?> updateType(@RequestBody ATypeEntity aTypeEntity) {
        return aTypeService.updateType(aTypeEntity);
    }

    @Operation(summary = "删除文章类别")
    @PostMapping(value = "/delType")
    public Result<?> deleteType(@RequestParam("id") Integer id) {
        return aTypeService.deleteType(id);
    }


    @Operation(summary = "文章分页")
    @PostMapping(value = "/getType")
    public Result<?> getType(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        return aTypeService.getType(pageNum, pageSize);
    }

    @Operation(summary = "文章类别列表")
    @PostMapping(value = "/listType")
    public Result<?> listType() {
        return aTypeService.listType();
    }
}
