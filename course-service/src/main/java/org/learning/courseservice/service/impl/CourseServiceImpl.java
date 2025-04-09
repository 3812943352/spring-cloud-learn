/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-09 18:16:50
 * @FilePath: course-service/src/main/java/org/learning/courseservice/service/impl/CourseServiceImpl.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.commonmodule.resp.Result;
import lombok.SneakyThrows;
import org.learning.courseservice.entity.PaperEntity;
import org.learning.courseservice.mapper.CourseMapper;
import org.learning.courseservice.entity.CourseEntity;
import org.learning.courseservice.mapper.PaperMapper;
import org.learning.courseservice.service.CourseService;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 王博
 * @since 2025-03-13
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, CourseEntity> implements CourseService {

    private final PaperMapper paperMapper;


    @Value("${file.course-dir}")
    private String course;

    @Autowired
    public CourseServiceImpl(PaperMapper paperMapper) {
        this.paperMapper = paperMapper;
    }

    @SneakyThrows
    @Override
    public Result<?> saveFile(MultipartFile file, String name, CourseEntity courseEntity) {
        String projectRoot = System.getProperty("user.dir");
        String fullUploadDir = projectRoot + "/" + this.course;
        Path path = Paths.get(fullUploadDir);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                return Result.failure(202, "无法创建目录: " + path);
            }
        }
        Path targetLocation = path.resolve(name);
        if (Files.exists(targetLocation)) {
            return Result.failure(202, "文件已存在: " + name);
        }
        try (InputStream inputStream = file.getInputStream()) {
            // 将输入流中的数据复制到目标位置
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            this.saveData(courseEntity, name, Path.of(name));
        } catch (InterruptedIOException e) {
            // 处理由于线程中断引起的IO异常
            Thread.currentThread().interrupt(); // 重新设置中断状态
            try {
                Files.deleteIfExists(targetLocation);
            } catch (IOException deleteException) {
                return Result.failure(204, "无法删除的不完整文件: " + name + ", 原因: " + deleteException.getMessage());
            }
            return Result.failure(203, "文件上传被中断: " + name);
        } catch (IOException e) {
            // 处理其他IO异常
            try {
                Files.deleteIfExists(targetLocation);
            } catch (IOException deleteException) {
                return Result.failure(204, "无法删除不完整的文件: " + name + ", 原因: " + deleteException.getMessage());
            }
            return Result.failure(202, "无法保存上传该文件: " + name);
        }
        return Result.success();
    }

    /**
     * 保存文件信息
     *
     * @param courseEntity 数据实体
     * @param name         文件名
     */
    public void saveData(CourseEntity courseEntity, String name, Path targetLocation) {
        CourseEntity existfile = this.getOne(new QueryWrapper<CourseEntity>().eq("name", name));
        if (existfile != null) {
            Result.failure(202, "该文件数据已存在: " + name);
            return;
        }
        long date = System.currentTimeMillis() / 1000;
        courseEntity.setCover(String.valueOf(targetLocation));
        courseEntity.setCreated(date);
        courseEntity.setUpdated(date);

        try {
            this.saveOrUpdate(courseEntity, new QueryWrapper<CourseEntity>().eq("ID", courseEntity.getId()));
            Result.success("文件数据保存成功");
        } catch (Exception e) {
            Result.failure(202, "该文件数据保存失败: " + name);
        }
    }

    @Override
    public Result<?> get(int pageNum, int pageSize) {
        try {
            Page<CourseEntity> resultPage = new Page<>(pageNum, pageSize);
            QueryWrapper<CourseEntity> queryWrapper = new QueryWrapper<>();
            resultPage = this.page(resultPage, queryWrapper);
            return Result.success(resultPage);
        } catch (Exception e) {
            return Result.failure(202, "查询失败" + e);
        }
    }

    @Override
    public Result<?> update(MultipartFile file, String name, CourseEntity courseEntity) {
        String projectRoot = System.getProperty("user.dir");
        // 拼接完整路径
        String fullUploadDir = projectRoot + "/" + this.course;
        Path path = Paths.get(fullUploadDir);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                return Result.failure(202, "无法创建目录: " + path);
            }
        }
        if (file == null || file.isEmpty()) {
            int iD = courseEntity.getId();
            this.saveOrUpdate(courseEntity, new QueryWrapper<CourseEntity>().eq("ID", iD));
            String oriname = this.getOne(new QueryWrapper<CourseEntity>().eq("ID", iD)).getName();
            return Result.success(oriname + "数据信息更新成功");
        }
        int iD = courseEntity.getId();
        String oriName = this.getOne(new QueryWrapper<CourseEntity>().eq("ID", iD)).getCover();
        Path targetLocation = path.resolve(oriName);
        Path newPath = path.resolve(name);
        this.saveData(courseEntity, name, Path.of(name));

        try (InputStream inputStream = file.getInputStream()) {
            boolean isDeleted = Files.deleteIfExists(targetLocation);
            Files.copy(inputStream, newPath, StandardCopyOption.REPLACE_EXISTING);
            if (isDeleted) {
                return Result.success("该文件与文件数据更新成功: " + name);
            } else {
                return Result.success("该文件更新成功但无法删除原文件: " + name);
            }
        } catch (InterruptedIOException e) {
            // 处理由于线程中断引起的IO异常
            Thread.currentThread().interrupt(); // 重新设置中断状态
            return Result.failure(203, "文件上传被中断: " + name + "请重试");
        } catch (IOException e) {
            return Result.failure(202, "该文件上传失败：" + name + "错误：" + e);
        }
    }

    @Override
    public Result<?> del(int iD) {
        PaperEntity paperEntity = paperMapper.selectOne(new QueryWrapper<PaperEntity>().eq("course", iD));
        paperEntity.setCourse(null);
        paperMapper.update(paperEntity, new QueryWrapper<PaperEntity>().eq("course", iD));

        String filename = this.getOne(new QueryWrapper<CourseEntity>().eq("ID", iD)).getCover();
        String projectRoot = System.getProperty("user.dir");
        String fullUploadDir = projectRoot + "/" + this.course;
        Path path = Paths.get(fullUploadDir);
        Path targetLocation = path.resolve(filename);
        System.out.println(targetLocation);
        boolean isDel = this.removeById(iD);
        try {
            boolean isDeleted = Files.deleteIfExists(targetLocation);
            if (isDeleted) {
                return Result.success("文件与数据删除成功" + filename);
            }
            return Result.failure(202, "删除失败: " + filename);
        } catch (MyBatisSystemException e) {

            // 记录根本原因
            Throwable rootCause = e.getRootCause();
            String errorMessage = "删除记录时发生错误: " + (rootCause != null ? rootCause.getMessage() : e.getMessage());
            return Result.failure(202, errorMessage);
        } catch (IOException e) {
            if (isDel) {

                return Result.failure(202, "删除成功但无法删除文件: " + filename + "错误：" + e);
            }
            return Result.failure(202, "删除失败: " + filename + "错误：" + e);
        }
    }

    @Override
    public Result<?> blur(int pageNum, int pageSize, String word) {
        Page<CourseEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<CourseEntity> queryWrapper = new QueryWrapper<>();
        if (word != null && !word.isEmpty()) {
            queryWrapper.or(wrapper -> wrapper
                    .like("name", word)
                    .or().like("des", word)

            );
        }
        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage, "请求成功");
    }

    @Override
    public Result<?> date(int pageNum, int pageSize, long startTime, long endTime) {
        Page<CourseEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<CourseEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("created", startTime, endTime);
        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage);
    }


    @Override
    public Result<?> listCourse() {
        // 查询所有 CourseEntity 数据，选择 ID、name 和 paper 字段
        List<CourseEntity> courseEntities = this.list(new QueryWrapper<CourseEntity>()
                .select("ID", "name", "paper"));

        // 创建 formattedList 列表，用于存储格式化后的数据
        List<Map<String, Object>> formattedList = new ArrayList<>();

        // 创建一个新的列表，用于存储 paper 为 0 的数据
        List<Map<String, Object>> paper0List = new ArrayList<>();

        for (CourseEntity course : courseEntities) {
            Map<String, Object> map = new HashMap<>();
            map.put("value", course.getId());
            map.put("label", course.getName());
            map.put("paper", course.getPaper());
            formattedList.add(map);

            // 如果 paper 字段值为 0，则将其加入到 paper0List 中
            if (course.getPaper() != null && course.getPaper() == 0) {
                Map<String, Object> paper0Map = new HashMap<>();
                paper0Map.put("value", course.getId());
                paper0Map.put("label", course.getName());
                paper0Map.put("paper", course.getPaper());
                paper0List.add(paper0Map);
            }
        }

        // 构造返回结果，包含 formattedList 和 paper0List
        Map<String, Object> resultData = new HashMap<>();
        resultData.put("formattedList", formattedList);
        resultData.put("paper0List", paper0List);

        return Result.success(resultData);
    }

    @Override
    public Result<?> getCourseByType(int pageNum, int pageSize, int type) {
        Page<CourseEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<CourseEntity> queryWrapper = new QueryWrapper<CourseEntity>()
                .eq("type", type)
                .eq("paper", 1)
                .isNotNull("tem")
                .and(wrapper -> wrapper.ne("JSON_LENGTH(video)", 0));

        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage);
    }

    @Override
    public Result<?> getCourseById(int id) {
        CourseEntity courseEntity = this.getOne(new QueryWrapper<CourseEntity>().eq("ID", id));
        return Result.success(courseEntity);
    }

    @Override
    public Result<?> userHome() {
        List<CourseEntity> list = this.list(new QueryWrapper<CourseEntity>()
                .select("id", "name", "cover")
                .eq("isShow", 1)
                .eq("paper", 1)
                .isNotNull("tem")
                .and(wrapper -> wrapper.ne("JSON_LENGTH(video)", 0))
        );
        return Result.success(list);
    }


    @Override
    public Result<?> coursePrice(int id) {
        CourseEntity courseEntity = this.getOne(new QueryWrapper<CourseEntity>().eq("id", id));
        double price = courseEntity.getPrice();
        return Result.success(price);
    }

    @Override
    public Result<?> videoList(int id) {
        CourseEntity courseEntity = this.getOne(new QueryWrapper<CourseEntity>().eq("id", id));
        List<Integer> videoList = courseEntity.getVideo();
        return Result.success(videoList);
    }

    @Override
    public Result<?> getCourseList(List<Integer> courseList) {
        List<CourseEntity> courseEntities = this.list(new QueryWrapper<CourseEntity>().in("id", courseList));
        return Result.success(courseEntities);
    }

}
