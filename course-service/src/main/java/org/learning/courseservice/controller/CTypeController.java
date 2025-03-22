/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-21 16:31:42
 * @FilePath: course-service/src/main/java/org/learning/courseservice/controller/CTypeController.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.controller;

import com.common.commonmodule.resp.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.learning.courseservice.entity.CTypeEntity;
import org.learning.courseservice.service.CTypeService;
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
@RequestMapping("/course")
public class CTypeController {
    private final CTypeService cTypeService;

    public CTypeController(@RequestBody CTypeService cTypeService) {
        this.cTypeService = cTypeService;
    }

    @Operation(summary = "创建课程类别")
    @PostMapping(value = "/addType")
    public Result<?> addType(@RequestBody CTypeEntity cTypeEntity) {
        return cTypeService.addType(cTypeEntity);
    }

    @Operation(summary = "更新课程类别")
    @PostMapping(value = "/updateType")
    public Result<?> updateType(@RequestBody CTypeEntity cTypeEntity) {
        return cTypeService.updateType(cTypeEntity);
    }

    @Operation(summary = "删除课程类别")
    @PostMapping(value = "/delType")
    public Result<?> deleteType(@RequestParam("id") Integer id) {
        return cTypeService.deleteType(id);
    }


    @Operation(summary = "课程分页")
    @PostMapping(value = "/getType")
    public Result<?> getType(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        return cTypeService.getType(pageNum, pageSize);
    }
}
