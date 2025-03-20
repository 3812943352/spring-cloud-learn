/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 16:01:19
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-18 16:46:46
 * @FilePath: course-service/src/main/java/org/learning/courseservice/controller/PaperController.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.controller;

import com.common.commonmodule.resp.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.learning.courseservice.entity.PaperEntity;
import org.learning.courseservice.service.PaperService;
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
@RequestMapping("/course")
public class PaperController {

    private final PaperService paperService;

    public PaperController(PaperService paperService) {
        this.paperService = paperService;
    }

    @Operation(summary = "创建试卷")
    @PostMapping(value = "/addPaper")
    public Result<?> addPaper(@RequestBody PaperEntity paperEntity) {
        return paperService.addPaper(paperEntity);
    }

    @Operation(summary = "更新试卷")
    @PostMapping(value = "/updatePaper")
    public Result<?> updatePaper(@RequestBody PaperEntity paperEntity) {
        return paperService.updatePaper(paperEntity);
    }

    @Operation(summary = "删除试卷")
    @PostMapping(value = "/delPaper")
    public Result<?> delPaper(@RequestParam("id") int id, @RequestParam("course") int course) {
        return paperService.delPaper(id, course);
    }

    @Operation(summary = "试卷分页")
    @PostMapping(value = "/getPaper")
    public Result<?> getPaper(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
        return paperService.get(pageNum, pageSize);
    }

    @Operation(summary = "模糊查询课程")
    @PostMapping(value = "/blurPaper")
    public Result<?> blurCourse(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                @RequestParam("word") String word) {
        return this.paperService.blur(pageNum, pageSize, word);
    }

    @Operation(summary = "根据时间查询课程")
    @PostMapping(value = "/datePaper")
    public Result<?> dateCourse(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                @RequestParam("start") long startTime,
                                @RequestParam("end") long endTime) {
        return this.paperService.date(pageNum, pageSize, startTime, endTime);
    }

    @Operation(summary = "课程列表")
    @PostMapping(value = "/listPaper")
    public Result<?> listCourse() {
        return this.paperService.listCourse();
    }
}
