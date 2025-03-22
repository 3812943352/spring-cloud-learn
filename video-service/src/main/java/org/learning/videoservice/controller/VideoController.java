/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-22 11:13:44
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

//    @GetMapping("/uploadedChunks")
//    public Result<?> getUploadedChunks(@RequestParam String fileName,
//                                       @RequestParam int totalChunks) {
//        Path tempDir = buildCategoryPath(category).resolve("temp");
//        List<Integer> uploaded = IntStream.range(0, totalChunks)
//                .filter(i -> Files.exists(
//                        tempDir.resolve(String.format("%s_part_%d", fileName, i))
//                )).boxed().collect(Collectors.toList());
//        return Result.success(uploaded);
//    }
}
