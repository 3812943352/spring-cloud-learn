/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-04-03 01:37:15
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-08 03:22:53
 * @FilePath: video-service/src/main/java/org/learning/videoservice/service/impl/ProgressServiceImpl.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.videoservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.commonmodule.resp.Result;
import org.learning.videoservice.entity.ProgressEntity;
import org.learning.videoservice.entity.VideoEntity;
import org.learning.videoservice.feignClient.CourseServiceFeignClient;
import org.learning.videoservice.mapper.ProgressMapper;
import org.learning.videoservice.mapper.VideoMapper;
import org.learning.videoservice.service.ProgressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 王博
 * @since 2025-04-03
 */
@Service
public class ProgressServiceImpl extends ServiceImpl<ProgressMapper, ProgressEntity> implements ProgressService {

    private final CourseServiceFeignClient courseServiceFeignClient;
    private final ProgressMapper progressMapper;
    private final VideoMapper videoMapper;

    public ProgressServiceImpl(CourseServiceFeignClient courseServiceFeignClient, ProgressMapper progressMapper, VideoMapper videoMapper) {
        this.courseServiceFeignClient = courseServiceFeignClient;
        this.progressMapper = progressMapper;
        this.videoMapper = videoMapper;
    }

    @Override
    @Transactional
    public Result<?> addOrUpdateProgress(ProgressEntity progressEntity) {
        try {
            long date = System.currentTimeMillis() / 1000;
            // 查询是否存在对应的进度记录
            ProgressEntity progress = this.getOne(new QueryWrapper<ProgressEntity>()
                    .eq("user", progressEntity.getUser())
                    .eq("video", progressEntity.getVideo()));
            if (progress == null) {
                // 如果不存在，则新增记录
                progress = new ProgressEntity();
                progress.setUser(progressEntity.getUser());
                progress.setVideo(progressEntity.getVideo());
                progress.setProgress(progressEntity.getProgress());
                progress.setCreated(date);
                progress.setUpdated(date);
                if (progressEntity.getProgress() >= 95) {
                    progress.setIsDone(1);
                }
                this.save(progress);
            } else {
                // 如果已存在，则更新记录
                if (progressEntity.getProgress() > progress.getProgress()) {
                    progress.setProgress(progressEntity.getProgress());
                    progress.setUpdated(date);
                    if (progressEntity.getProgress() >= 95) {
                        progress.setIsDone(1);
                    }
                    this.updateById(progress);
                }
            }
            return Result.success(null, null, 200);
        } catch (Exception e) {
            return Result.success(null, null, 200);
        }
    }

    @Override
    public Result<?> userVideoRecord(int user) {
        List<ProgressEntity> list = this.list(new QueryWrapper<ProgressEntity>().eq("user", user));

        return Result.success(list);
    }

    @Override
    public Result<?> videoProgress(int user, int video) {
        ProgressEntity progressEntity = this.getOne(new QueryWrapper<ProgressEntity>()
                .eq("user", user)
                .eq("video", video));
        return Result.success(progressEntity);
    }

    @Override
    public Result<?> isDone(int user, int course) {
        Result<?> videoListResult = courseServiceFeignClient.videoList(course);
        List<Integer> videoList = (List<Integer>) videoListResult.getData();
        if (videoList != null && !videoList.isEmpty()) {
            System.out.println("videoList:" + videoList + ",course:" + course);

            List<ProgressEntity> progressList = progressMapper.selectList(new QueryWrapper<ProgressEntity>()
                    .in("video", videoList)
                    .eq("user", user));
            boolean allDone = progressList.stream()
                    .allMatch(progress -> progress.getIsDone() == 1);
            if (allDone) {
                return Result.success(true);
            } else {
                return Result.success(false);
            }
        } else {
            return Result.success(false);
        }

    }

    @Override
    public Result<?> courseProgress(int user, int course) {

        // 获取课程的视频列表
        Result<?> videoListResult = courseServiceFeignClient.videoList(course);
        List<Integer> videoList = (List<Integer>) videoListResult.getData();
        if (videoList == null || videoList.isEmpty()) {
            return Result.failure("该课程没有视频内容");
        }

        // 初始化结果列表
        List<Object> result = new ArrayList<>();

        // 批量查询其他视频的播放进度
        List<VideoEntity> videoEntities = videoMapper.selectList(new QueryWrapper<VideoEntity>()
                .in("ID", videoList)
                .select("content_path", "title", "created", "ID"));

        // 批量查询所有视频的播放进度
        List<ProgressEntity> progressList = progressMapper.selectList(new QueryWrapper<ProgressEntity>()
                .in("video", videoList)
                .eq("user", user));

        // 将进度记录按 videoId 映射到 Map 中
        Map<Integer, ProgressEntity> progressMap = progressList.stream()
                .collect(Collectors.toMap(ProgressEntity::getVideo, Function.identity(), (existing, replacement) -> existing));
        List<Object> otherVideos = new ArrayList<>();
        for (VideoEntity videoEntity : videoEntities) {


            // 获取对应视频的进度信息（如果不存在则为 null）
            ProgressEntity otherProgressEntity = progressMap.getOrDefault(videoEntity.getId(), null);

            // 将视频信息和进度信息组合
            Map<String, Object> videoInfo = new HashMap<>();
            videoInfo.put("video", videoEntity);
            videoInfo.put("progress", otherProgressEntity);
            otherVideos.add(videoInfo);
        }

        result.add(otherVideos);

        // 返回最终结果
        return Result.success(result, null, 200);
    }
}
