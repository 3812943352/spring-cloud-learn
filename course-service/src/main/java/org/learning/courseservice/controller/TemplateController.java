/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 16:38:27
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-27 21:08:37
 * @FilePath: course-service/src/main/java/org/learning/courseservice/controller/TemplateController.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.controller;

import com.common.commonmodule.resp.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.learning.courseservice.entity.ExpertEntity;
import org.learning.courseservice.entity.TemplateEntity;
import org.learning.courseservice.service.ExpertService;
import org.learning.courseservice.service.TemplateService;
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
 * @since 2025-03-14
 */
@RestController
@RequestMapping("/course")
public class TemplateController {
    private static final List<String> SUPPORTED_EXTENSIONS = Arrays.asList(".webp", ".png", ".jpg", ".jpeg");

    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @Operation(summary = "创建模板")
    @PostMapping(value = "/addTemplate")
    public Result<?> add(@ModelAttribute TemplateEntity templateEntity, @RequestParam("file") MultipartFile file) {
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
        return this.templateService.add(templateEntity, name, file);
    }

    @Operation(summary = "模板列表")
    @PostMapping(value = "/getTemplate")
    public Result<?> getCourse(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
        return this.templateService.get(pageNum, pageSize);
    }

    @Operation(summary = "更新模板")
    @PostMapping(value = "/updateTemplate")
    public Result<?> updateCourse(@ModelAttribute TemplateEntity templateEntity, @RequestParam(value = "file", required = false) MultipartFile file) {
        if (file == null || file.isEmpty()) {
            String name = "";
            return this.templateService.update(file, name, templateEntity);
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
        return this.templateService.update(file, name, templateEntity);
    }

    @Operation(summary = "删除模板")
    @PostMapping(value = "/delTemplate")
    public Result<?> delCourse(@RequestParam("id") Integer id) {
        return this.templateService.del(id);
    }


    @Operation(summary = "模糊查询模板")
    @PostMapping(value = "/blurTemplate")
    public Result<?> blurCourse(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                @RequestParam("word") String word) {
        return this.templateService.blur(pageNum, pageSize, word);
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
