/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-01 13:42:28
 * @FilePath: course-service/src/main/java/org/learning/courseservice/controller/ExpertController.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.controller;

import com.common.commonmodule.resp.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.learning.courseservice.entity.CourseEntity;
import org.learning.courseservice.entity.ExpertEntity;
import org.learning.courseservice.service.CourseService;
import org.learning.courseservice.service.ExpertService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

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
public class ExpertController {
    private static final List<String> SUPPORTED_EXTENSIONS = Arrays.asList(".webp", ".png", ".jpg", ".jpeg");

    private final ExpertService expertService;

    public ExpertController(ExpertService expertService) {
        this.expertService = expertService;
    }

    @Operation(summary = "创建专家")
    @PostMapping(value = "/addExpert")
    public Result<?> add(@ModelAttribute ExpertEntity expertEntity, @RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Result.failure(202, "请不要上传空文件！");
        }
        if (!validFile(file)) {
            return Result.failure(202, "请上传图片！");
        }
        String name = file.getOriginalFilename();
        long size = file.getSize();
        if (size > 1024 * 1024 * 5) {
            return Result.failure(202, "文件大小不能超过5M！");
        }

        if (file.getOriginalFilename() == null) {
            return Result.failure(202, "文件名不能为空！");
        }
        return this.expertService.add(expertEntity, name, file);
    }

    @Operation(summary = "专家列表")
    @PostMapping(value = "/getExpert")
    public Result<?> getCourse(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
        return this.expertService.get(pageNum, pageSize);
    }

    @Operation(summary = "更新专家")
    @PostMapping(value = "/updateExpert")
    public Result<?> updateCourse(@ModelAttribute ExpertEntity expertEntity, @RequestParam(value = "file", required = false) MultipartFile file) {
        if (file == null || file.isEmpty()) {
            String name = "";
            return this.expertService.update(file, name, expertEntity);
        }
        System.out.println(file.getOriginalFilename());
        System.out.println(expertEntity.getArt());
        if (!validFile(file)) {
            return Result.failure(202, "请上传图片！");
        }
        String name = file.getOriginalFilename();
        long size = file.getSize();
        if (size > 1024 * 1024 * 5) {
            return Result.failure(202, "文件大小不能超过5M！");
        }
        if (file.getOriginalFilename() == null) {
            return Result.failure(202, "文件名不能为空！");
        }
        return this.expertService.update(file, name, expertEntity);
    }

    @Operation(summary = "删除专家")
    @PostMapping(value = "/delExpert")
    public Result<?> delCourse(@RequestParam("id") Integer id) {
        return this.expertService.del(id);
    }


    @Operation(summary = "模糊查询专家")
    @PostMapping(value = "/blurExpert")
    public Result<?> blurCourse(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                @RequestParam("word") String word) {
        return this.expertService.blur(pageNum, pageSize, word);
    }

    @Operation(summary = "根据时间查询专家")
    @PostMapping(value = "/dateExpert")
    public Result<?> dateCourse(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                @RequestParam("start") long startTime,
                                @RequestParam("end") long endTime) {
        return this.expertService.date(pageNum, pageSize, startTime, endTime);
    }

    @Operation(summary = "首页专家列表")
    @PostMapping(value = "/listEx")
    public Result<?> list() {
        return this.expertService.listEx();
    }

    public boolean validFile(MultipartFile file) {

        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            String extension = this.getFileExtension(originalFilename).toLowerCase();
            return SUPPORTED_EXTENSIONS.contains(extension);
        }
        return false;
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return filename.substring(lastDotIndex);
        }
        return "";
    }
}
