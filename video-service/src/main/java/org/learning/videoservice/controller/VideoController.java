/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-23 18:29:03
 * @FilePath: video-service/src/main/java/org/learning/videoservice/controller/VideoController.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.videoservice.controller;

import com.common.commonmodule.resp.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.learning.videoservice.entity.VideoEntity;
import org.learning.videoservice.service.VideoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 王博
 * @since 2025-03-13
 */
@RestController
@RequestMapping("/video")
public class VideoController {
    private static final List<String> SUPPORTED_EXTENSIONS = Arrays.asList(".mp4", ".webm", ".mov", ".flv", ".avi");

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @Operation(summary = "视频上传")
    @PostMapping(value = "/uploadVideo")
    public Result<?> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @ModelAttribute VideoEntity videoEntity,
            @RequestParam("num") Integer chunkNumber,
            @RequestParam("total") Integer totalChunks,
            @RequestParam("hash") String fileHash
    ) {
        return videoService.saveVideo(file, "course", videoEntity, name, chunkNumber, totalChunks, fileHash);
    }


    @Operation(summary = "更新视频")
    @PostMapping(value = "/updateVideo")
    public Result<?> updateCourse(@RequestParam(value = "file", required = false) MultipartFile file,
                                  @RequestParam(value = "name", required = false) String name,
                                  @ModelAttribute VideoEntity videoEntity,
                                  @RequestParam(value = "num", required = false) Integer chunkNumber,
                                  @RequestParam(value = "total", required = false) Integer totalChunks,
                                  @RequestParam(value = "hash", required = false) String fileHash) {
        if (file == null || file.isEmpty()) {
            return this.videoService.update(videoEntity, fileHash);
        }

        if (!validFile(file)) {
            return Result.failure(202, "请上传视频！");
        }

        return this.videoService.updateVideo(file, "course", videoEntity, name, chunkNumber, totalChunks, fileHash);
    }


    @Operation(summary = "删除视频")
    @PostMapping(value = "delVideo")
    public Result<?> delVideo(@RequestParam("id") int id) {
        return this.videoService.deleteVideo(id);

    }

    @Operation(summary = "视频信息分页")
    @PostMapping(value = "getVideo")
    public Result<?> getVideo(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        return this.videoService.getVideo(pageNum, pageSize);
    }

    @Operation(summary = "视频信息模糊查询")
    @PostMapping(value = "blurVideo")
    public Result<?> blurVideo(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize, @RequestParam("word") String word) {
        return this.videoService.blur(pageNum, pageSize, word);
    }

    @Operation(summary = "视频信息日期查询")
    @PostMapping(value = "dateVideo")
    public Result<?> dateVideo(@RequestParam("pageNum") int pageNum, @RequestParam("pageNum") int pageSize, @RequestParam("pageNum") long startTime, @RequestParam("pageNum") long endTime) {
        return this.videoService.date(pageNum, pageSize, startTime, endTime);
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
