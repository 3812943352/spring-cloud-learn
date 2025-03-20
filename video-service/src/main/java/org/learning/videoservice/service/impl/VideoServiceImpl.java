/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-15 09:50:51
 * @FilePath: video-service/src/main/java/org/learning/videoservice/service/impl/VideoServiceImpl.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.videoservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.commonmodule.resp.Result;
import org.learning.videoservice.mapper.VideoMapper;
import org.learning.videoservice.entity.VideoEntity;
import org.learning.videoservice.service.VideoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

import static java.nio.file.StandardOpenOption.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 王博
 * @since 2025-03-13
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, VideoEntity> implements VideoService {
    @Value("${file.videos-dir}") // 修复可能的注解参数缺少大括号的问题
    private String videosDir;


    @Override
    public Result<?> saveVideo(MultipartFile file, String category,
                               String name, int chunkNumber, int totalChunks, String fileHash) {


        try {
            // 生成唯一文件名
            String uniqueFileName = generateUniqueName(name, fileHash);
            Path storagePath = buildCategoryPath(category);

            // 处理分片上传
            return handleChunkUpload(file, storagePath, uniqueFileName, chunkNumber, totalChunks);
        } catch (Exception e) {
            return Result.failure(500, "上传异常: " + e.getMessage());
        }
    }
    
    private String generateUniqueName(String originalName, String fileHash) {
        // 提取文件扩展名
        String extension = "";
        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalName.substring(dotIndex);
        }

        return LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)
                + "_" + fileHash.substring(0, 8)
                + "_" + extension; // 保留原始扩展名
    }

    // 构建分类存储路径（包含日期细分）
    private Path buildCategoryPath(String category) {
        return Paths.get(
                System.getProperty("user.dir"),
                videosDir,
                URLEncoder.encode(category, StandardCharsets.UTF_8)
        );
    }

    // 分片处理核心逻辑
    private Result<?> handleChunkUpload(MultipartFile file, Path storagePath,
                                        String fileName, int chunkNumber,
                                        int totalChunks) {
        try {
            // 创建基础目录和临时目录
            Files.createDirectories(storagePath);
            Path tempDir = storagePath.resolve("temp");
            Files.createDirectories(tempDir);

            // 构建分片路径
            Path chunkPath = tempDir.resolve(fileName + "_part_" + chunkNumber);
            File chunkFile = chunkPath.toFile();

            // 检查并删除已存在的分片（防止冲突）
            if (chunkFile.exists()) {
                return Result.failure(200, "分片已存在");
            }

            // 写入分片
            file.transferTo(chunkFile);

            // 检查合并条件
            if (isAllChunksReady(storagePath, fileName, totalChunks)) {
                return mergeChunks(storagePath, fileName, totalChunks);
            }

            return Result.success("分片接收成功", chunkNumber + "/" + totalChunks);
        } catch (IOException e) {
            return Result.failure(500, "上传异常: " + e.getMessage());
        }
    }

    // 分片完整性校验
    private boolean isAllChunksReady(Path storagePath, String fileName,
                                     int totalChunks) {
        Path tempDir = storagePath.resolve("temp");
        return IntStream.range(0, totalChunks)
                .allMatch(i -> Files.exists(
                        tempDir.resolve(fileName + "_part_" + i)
                ));
    }

    // 分片合并方法
    private Result<?> mergeChunks(Path storagePath, String fileName,
                                  int totalChunks) {
        try {
            Path targetFile = storagePath.resolve(fileName);
            Path tempDir = storagePath.resolve("temp");

            try (FileChannel destChannel =
                         FileChannel.open(targetFile, CREATE, WRITE, APPEND)) {

                for (int i = 0; i < totalChunks; i++) {
                    Path chunk = tempDir.resolve(fileName + "_part_" + i);
                    try (FileChannel srcChannel = FileChannel.open(chunk, READ)) {
                        srcChannel.transferTo(0, srcChannel.size(), destChannel);
                    }
                    Files.delete(chunk);
                }
            }
            Files.delete(tempDir);
            return Result.success("文件合并完成");
        } catch (IOException e) {
            return Result.failure(500, "合并失败: " + e.getMessage());
        }
    }
}