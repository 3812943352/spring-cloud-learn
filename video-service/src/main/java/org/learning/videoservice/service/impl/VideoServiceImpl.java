/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-23 18:39:02
 * @FilePath: video-service/src/main/java/org/learning/videoservice/service/impl/VideoServiceImpl.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.videoservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.commonmodule.resp.Result;
import org.learning.videoservice.mapper.VideoMapper;
import org.learning.videoservice.entity.VideoEntity;
import org.learning.videoservice.service.VideoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
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

    public static void deleteAll(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    // 删除文件
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    // 删除目录
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            System.out.println("Path does not exist: " + path);
        }
    }

    @Override
    public Result<?> saveVideo(MultipartFile file, String category, VideoEntity videoEntity,
                               String name, int chunkNumber, int totalChunks, String fileHash) {
        String title = videoEntity.getTitle();
        VideoEntity existfile = this.getOne(new QueryWrapper<VideoEntity>().eq("title", title));
        if (existfile != null && videoEntity.getId() == null) {
            return Result.failure(208, "该文件数据已存在: " + title);
        }
        try {
            // 生成唯一文件名
            String uniqueFileName = generateUniqueName(name, fileHash);
            Path storagePath = buildCategoryPath(category);
            // 处理分片上传
            return handleChunkUpload(file, storagePath, uniqueFileName, chunkNumber, totalChunks, videoEntity, fileHash);
        } catch (Exception e) {
            return Result.failure(202, "上传异常: " + e.getMessage());
        }
    }

    public boolean saveData(VideoEntity videoEntity, String fileHash) {

        long date = System.currentTimeMillis() / 1000;
        videoEntity.setCreated(date);
        videoEntity.setUpdated(date);
        videoEntity.setContentPath(fileHash);
        try {
            this.saveOrUpdate(videoEntity, new QueryWrapper<VideoEntity>().eq("ID", videoEntity.getId()));
            return true;
        } catch (Exception e) {

            return false;
        }
    }

    private String generateUniqueName(String originalName, String fileHash) {
        // 提取文件扩展名
        String extension = "";
        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalName.substring(dotIndex);
        }

        return fileHash
                + extension;
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
                                        int totalChunks, VideoEntity videoEntity, String fileHash) {
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


                return mergeChunks(storagePath, fileName, totalChunks, videoEntity, fileHash);
            }

            return Result.success("分片接收成功" + chunkNumber + "/" + totalChunks);
        } catch (IOException e) {
            return Result.failure(202, "上传异常: " + e.getMessage());
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

    //分片合并方法
    private Result<?> mergeChunks(Path storagePath, String fileName,
                                  int totalChunks, VideoEntity videoEntity, String fileHash) {
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
            if (!this.saveData(videoEntity, fileHash)) {
                Files.delete(targetFile);
                return Result.failure(208, "上传失败，请重新上传");
            }

            // 转换MP4到HLS的逻辑


            return convertMp4ToHls(targetFile.toString(), storagePath.toString(), fileHash);
        } catch (IOException e) {
            return Result.failure(208, "合并失败: " + e.getMessage());
        }
    }

    //FFMPEG
    private Result<?> convertMp4ToHls(String inputFilePath, String outputDirectory, String originalFileName) {
        try {
            // 创建输出目录，以原文件名为名
            Path outputDirPath = Paths.get(outputDirectory, originalFileName);
            Files.createDirectories(outputDirPath);

            // 构建FFmpeg命令，确保路径中的空格被正确转义
            String baseUrl = "http://127.0.0.1:9999/video/videos/course/" + originalFileName + "/";
            String ffmpegCommand = String.format(
                    "ffmpeg -i \"%s\" -c:v copy -c:a copy -threads 8 -start_number 0 -hls_time 10 -hls_list_size 0 -hls_base_url \"%s\" -f hls \"%s/output.m3u8\"",
                    inputFilePath.replaceAll(" ", "\\\\ "),
                    baseUrl,
                    outputDirPath.toString().replaceAll(" ", "\\\\ ")
            );

            // 执行命令并捕获输出
            ProcessBuilder pb = new ProcessBuilder(ffmpegCommand.split(" "));
            pb.redirectErrorStream(true);

            Process process = pb.start();

            // 读取进程输出（包括错误信息）
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // 等待进程完成
            int exitCode = process.waitFor();

            // 删除原始MP4文件
            File inputFile = new File(inputFilePath);
            if (exitCode != 0) {
                try {
                    Files.delete(inputFile.toPath());
                } catch (IOException e) {
                    System.out.println("删除文件时发生异常: " + e.getMessage());
                }
                return Result.failure(208, "视频转换执行失败，输出:\n" + output);
            }

            if (inputFile.exists() && !inputFile.delete()) {
                return Result.failure(208, "无法删除原始MP4文件: " + originalFileName + ".mp4");
            }

            return Result.success(null, "文件上传、保存数据及转换为HLS格式成功", 207);
        } catch (Exception e) {
            return Result.failure(208, "上传失败: " + e.getMessage());
        }
    }

    @Override
    public Result<?> deleteVideo(int id) {
        // 查询视频记录
        VideoEntity videoEntity = this.getOne(new QueryWrapper<VideoEntity>().eq("ID", id));
        if (videoEntity == null) {
            return Result.failure(202, "文件不存在1");
        }

        try {
            // 获取文件路径
            String projectRoot = System.getProperty("user.dir");
            String p = videoEntity.getContentPath();
            String fullPath = projectRoot + videosDir + "/course/" + p;
            Path path = Paths.get(fullPath);
            System.out.println(path);
            // 检查文件是否存在
            if (!Files.exists(path)) {
                return Result.failure(202, "文件不存在2");
            }

            // 删除文件
            deleteAll(path);
            // 删除数据库记录
            this.removeById(id);

            return Result.success(null, "文件删除成功", 200);
        } catch (NoSuchFileException e) {
            return Result.failure(202, "文件不存在");
        } catch (IOException e) {
            return Result.failure(202, "文件删除失败，请稍后重试" + e);
        } catch (Exception e) {
            return Result.failure(202, "未知错误：" + e.getMessage());
        }
    }

    @Override
    public Result<?> getVideo(int pageNum, int pageSize) {
        try {
            Page<VideoEntity> resultPage = new Page<>(pageNum, pageSize);
            QueryWrapper<VideoEntity> queryWrapper = new QueryWrapper<>();
            resultPage = this.page(resultPage, queryWrapper);
            return Result.success(resultPage);
        } catch (Exception e) {
            return Result.failure(202, "查询失败" + e.getMessage());
        }
    }

    @Override
    public Result<?> update(VideoEntity videoEntity, String fileHash) {
        long date = System.currentTimeMillis() / 1000;
        videoEntity.setUpdated(date);
        videoEntity.setContentPath(fileHash);
        try {
            this.saveOrUpdate(videoEntity, new QueryWrapper<VideoEntity>().eq("ID", videoEntity.getId()));
            return Result.success(null, "视频数据更新成功", 200);
        } catch (Exception e) {

            return Result.failure(202, "视频数据更新失败" + e);
        }
    }

    @Override
    public Result<?> updateVideo(MultipartFile file, String category, VideoEntity videoEntity,
                                 String name, int chunkNumber, int totalChunks, String fileHash) {
        String title = videoEntity.getTitle();
        VideoEntity existfile = this.getOne(new QueryWrapper<VideoEntity>().eq("title", title));
        if (existfile == null) {
            return Result.failure(208, "该文件数据不存在！ ");

        }
        try {
            String projectRoot = System.getProperty("user.dir");
            String p = existfile.getContentPath();
            String fullPath = projectRoot + videosDir + "/course/" + p;
            Path path = Paths.get(fullPath);
            if (!Files.exists(path)) {
                return Result.failure(202, "文件不存在");
            }

            // 删除文件
            deleteAll(path);
            // 生成唯一文件名
            String uniqueFileName = generateUniqueName(name, fileHash);
            Path storagePath = buildCategoryPath(category);
            // 处理分片上传
            return handleChunkUpload(file, storagePath, uniqueFileName, chunkNumber, totalChunks, videoEntity, fileHash);
        } catch (Exception e) {
            return Result.failure(202, "上传异常: " + e.getMessage());
        }
    }

    @Override
    public Result<?> blur(int pageNum, int pageSize, String word) {
        Page<VideoEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<VideoEntity> queryWrapper = new QueryWrapper<>();
        if (word != null && !word.isEmpty()) {
            queryWrapper.or(wrapper -> wrapper
                    .like("title", word)
                    .or().like("des", word)

            );
        }
        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage, "请求成功");
    }

    @Override
    public Result<?> date(int pageNum, int pageSize, long startTime, long endTime) {
        Page<VideoEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<VideoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("created", startTime, endTime);
        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage);
    }
}