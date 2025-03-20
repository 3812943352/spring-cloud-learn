/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-18 09:52:06
 * @FilePath: course-service/src/main/java/org/learning/courseservice/controller/CourseController.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.controller;

import com.common.commonmodule.resp.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.learning.courseservice.entity.CourseEntity;
import org.learning.courseservice.service.CourseService;
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
public class CourseController {
    private static final List<String> SUPPORTED_EXTENSIONS = Arrays.asList(".webp", ".png", ".jpg", ".jpeg");
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @Operation(summary = "创建课程")
    @PostMapping(value = "/addCourse")
    public Result<?> addCourse(@ModelAttribute CourseEntity courseEntity, @RequestParam("file") MultipartFile file) {
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
        return this.courseService.saveFile(file, name, courseEntity);
    }

    @Operation(summary = "课程列表")
    @PostMapping(value = "/getCourse")
    public Result<?> getCourse(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
        return this.courseService.get(pageNum, pageSize);
    }

    @Operation(summary = "更新课程")
    @PostMapping(value = "/updateCourse")
    public Result<?> updateCourse(@ModelAttribute CourseEntity courseEntity, @RequestParam(value = "file", required = false) MultipartFile file) {
        if (file == null || file.isEmpty()) {
            String name = "";
            return this.courseService.update(file, name, courseEntity);
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
        return this.courseService.update(file, name, courseEntity);
    }

    @Operation(summary = "删除课程")
    @PostMapping(value = "/delCourse")
    public Result<?> delCourse(@RequestParam("id") Integer id) {
        return this.courseService.del(id);
    }


    @Operation(summary = "模糊查询课程")
    @PostMapping(value = "/blurCourse")
    public Result<?> blurCourse(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                @RequestParam("word") String word) {
        return this.courseService.blur(pageNum, pageSize, word);
    }

    @Operation(summary = "根据时间查询课程")
    @PostMapping(value = "/dateCourse")
    public Result<?> dateCourse(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                @RequestParam("start") long startTime,
                                @RequestParam("end") long endTime) {
        return this.courseService.date(pageNum, pageSize, startTime, endTime);
    }

    @Operation(summary = "课程列表")
    @PostMapping(value = "/listCourse")
    public Result<?> listCourse() {
        return this.courseService.listCourse();
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
