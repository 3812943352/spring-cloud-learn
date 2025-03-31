/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 09:01:55
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-31 20:32:03
 * @FilePath: contents-service/src/main/java/org/learning/contentsservice/service/impl/ArticleServiceImpl.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.contentsservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.commonmodule.resp.Result;
import org.learning.contentsservice.entity.ArticleEntity;
import org.learning.contentsservice.mapper.ArticleMapper;
import org.learning.contentsservice.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.mybatis.spring.MyBatisSystemException;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 王博
 * @since 2025-03-14
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, ArticleEntity> implements ArticleService {
    @Value("${file.content-dir}")
    private String contentDir;
    @Value("${regex1}")
    private String regex1;
    @Value("${regex2}")
    private String rege2;

    @Override
    public Result<?> addArticle(MultipartFile img1, MultipartFile img2,
                                MultipartFile img3, MultipartFile img4,
                                MultipartFile img5, ArticleEntity articleEntity) {
        // 获取项目根目录并拼接目标路径
        String projectRoot = System.getProperty("user.dir");
        String fullUploadDir = projectRoot + "/" + this.contentDir;
        Path path = Paths.get(fullUploadDir);

        // 如果目标目录不存在，则创建目录
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                return Result.failure(202, "无法创建目录: " + path);
            }
        }

        // 将所有图片放入一个列表中，方便统一处理
        List<MultipartFile> images = Arrays.asList(img1, img2, img3, img4, img5);
        // 存储已上传的文件名
        List<String> uploadedFileNames = new ArrayList<>();

        // 遍历每个文件并处理上传逻辑
        for (MultipartFile img : images) {
            if (img != null && !img.isEmpty()) {
                String fileName = img.getOriginalFilename();
                Path targetLocation = path.resolve(fileName);
                System.out.println("targetLocation:" + targetLocation);
                System.out.println("path:" + path);
                // 检查文件是否已存在
                if (Files.exists(targetLocation)) {
                    // 回滚已上传的文件
                    rollbackUploadedFiles(path, uploadedFileNames);
                    return Result.failure(202, "文件已存在: " + fileName);
                }

                try (InputStream inputStream = img.getInputStream()) {
                    // 将输入流中的数据复制到目标位置
                    Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
                    // 记录已上传的文件名
                    uploadedFileNames.add(fileName);
                } catch (InterruptedIOException e) {
                    // 处理由于线程中断引起的IO异常
                    Thread.currentThread().interrupt(); // 重新设置中断状态
                    rollbackUploadedFiles(path, uploadedFileNames); // 回滚已上传的文件
                    return Result.failure(203, "文件上传被中断: " + fileName);
                } catch (IOException e) {
                    // 处理其他IO异常
                    rollbackUploadedFiles(path, uploadedFileNames); // 回滚已上传的文件
                    return Result.failure(202, "无法保存上传该文件: " + fileName);
                }
            }
        }

        // 所有文件上传成功，尝试保存数据
        try {
            saveData(articleEntity, uploadedFileNames);
            return Result.success("文章及图片保存成功");
        } catch (Exception e) {
            rollbackUploadedFiles(path, uploadedFileNames); // 数据保存失败，回滚已上传的文件
            return Result.failure(202, "文章数据保存失败: " + e.getMessage());
        }
    }

    // 回滚已上传的文件
    private void rollbackUploadedFiles(Path basePath, List<String> uploadedFileNames) {
        for (String fileName : uploadedFileNames) {
            Path filePath = basePath.resolve(fileName);
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                // 忽略删除失败的情况
                System.err.println("无法删除文件: " + fileName + ", 原因: " + e.getMessage());
            }
        }
    }

    // 保存数据的方法
    private void saveData(ArticleEntity articleEntity, List<String> uploadedFileNames) {
        // 检查是否已存在相同名称的文章
        ArticleEntity existFile = this.getOne(new QueryWrapper<ArticleEntity>()
                .eq("title", articleEntity.getTitle())
                .eq("type", articleEntity.getType()));
        if (existFile != null) {
            throw new RuntimeException("该文章数据已存在: " + articleEntity.getTitle());
        }

        // 设置时间戳
        long date = System.currentTimeMillis() / 1000;
        articleEntity.setCreated(date);
        articleEntity.setUpdated(date);

        // 获取原始 content
        String content = articleEntity.getContent();

        // 替换 content 中的图片链接
        if (content != null && !uploadedFileNames.isEmpty()) {
            content = replaceImageLinks(content, uploadedFileNames);
            articleEntity.setContent(content);
        }

        // 检查 img1 到 img5 是否有空位，并分配新图片
        List<String> imgFields = Arrays.asList(
                articleEntity.getImg1(),
                articleEntity.getImg2(),
                articleEntity.getImg3(),
                articleEntity.getImg4(),
                articleEntity.getImg5()
        );

        // 找出第一个为空的字段索引
        int firstEmptyIndex = -1;
        for (int i = 0; i < imgFields.size(); i++) {
            if (imgFields.get(i) == null || imgFields.get(i).isEmpty()) {
                firstEmptyIndex = i;
                break;
            }
        }

        // 如果没有空位且上传了新图片，则抛出异常
        if (firstEmptyIndex == -1 && !uploadedFileNames.isEmpty()) {
            throw new RuntimeException("图片数量已达到上限，不能超过五张");
        }

        // 分配新图片到空位
        for (String uploadedFileName : uploadedFileNames) {
            if (firstEmptyIndex >= 0 && firstEmptyIndex < 5) {
                switch (firstEmptyIndex) {
                    case 0:
                        articleEntity.setImg1(uploadedFileName);
                        break;
                    case 1:
                        articleEntity.setImg2(uploadedFileName);
                        break;
                    case 2:
                        articleEntity.setImg3(uploadedFileName);
                        break;
                    case 3:
                        articleEntity.setImg4(uploadedFileName);
                        break;
                    case 4:
                        articleEntity.setImg5(uploadedFileName);
                        break;
                }
                firstEmptyIndex++; // 移动到下一个空位
            } else {
                throw new RuntimeException("图片数量已达到上限，不能超过五张");
            }
        }

        // 保存或更新数据
        boolean success = this.saveOrUpdate(articleEntity, new QueryWrapper<ArticleEntity>().eq("ID", articleEntity.getId()));
        if (!success) {
            throw new RuntimeException("文章数据保存失败");
        }
    }

    private String replaceImageLinks(String content, List<String> uploadedFileNames) {
        // 匹配 Markdown 图片链接的正则表达式
        String regex = regex1;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        // 使用一个索引来匹配 uploadedFileNames 中的文件名
        int index = 0;
        StringBuffer replacedContent = new StringBuffer();

        while (matcher.find()) {
            if (index < uploadedFileNames.size()) {
                // 构造新的图片链接
                String newImageUrl = "![](http://localhost:9999/contents/images/" + uploadedFileNames.get(index) + ")";
                matcher.appendReplacement(replacedContent, newImageUrl);
                index++;
            } else {
                // 如果 uploadedFileNames 不足，保留原始链接
                matcher.appendReplacement(replacedContent, matcher.group(0));
            }
        }

        // 将剩余部分追加到结果中
        matcher.appendTail(replacedContent);

        return replacedContent.toString();
    }

    @Override
    public Result<?> getArticle(int pageNum, int pageSize) {
        try {
            Page<ArticleEntity> resultPage = new Page<>(pageNum, pageSize);
            QueryWrapper<ArticleEntity> queryWrapper = new QueryWrapper<>();
            resultPage = this.page(resultPage, queryWrapper);
            return Result.success(resultPage);
        } catch (Exception e) {
            return Result.failure(202, "查询失败" + e);
        }
    }

    @Override
    public Result<?> getArtById(int id) {
        ArticleEntity articleEntity = this.getOne(new QueryWrapper<ArticleEntity>().eq("id", id));
        if (articleEntity == null) {
            return Result.failure("该文章不存在");
        }
        articleEntity.setVisit(articleEntity.getVisit() + 1);
        this.updateById(articleEntity);

        return Result.success(articleEntity);
    }

    @Override
    public Result<?> getArtByType(int pageNum, int pageSize, int type) {
        try {
            Page<ArticleEntity> resultPage = new Page<>(pageNum, pageSize);
            QueryWrapper<ArticleEntity> queryWrapper = new QueryWrapper<ArticleEntity>().eq("type", type);
            resultPage = this.page(resultPage, queryWrapper);
            return Result.success(resultPage);
        } catch (Exception e) {
            return Result.failure(202, "查询失败" + e);
        }
    }

    @Override
    public Result<?> blur(int pageNum, int pageSize, String word) {
        Page<ArticleEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<ArticleEntity> queryWrapper = new QueryWrapper<>();
        if (word != null && !word.isEmpty()) {
            queryWrapper.or(wrapper -> wrapper
                    .like("title", word)

            );
        }
        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage, "请求成功");
    }

    @Override
    public Result<?> date(int pageNum, int pageSize, long startTime, long endTime) {
        Page<ArticleEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<ArticleEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("created", startTime, endTime);
        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage);
    }


    @Override
    public Result<?> del(int id) {
        // 查询文章实体
        ArticleEntity articleEntity = this.getOne(new QueryWrapper<ArticleEntity>().eq("ID", id));
        if (articleEntity == null) {
            return Result.failure("该文章不存在！");
        }

        // 获取图片路径
        String[] imgPaths = {
                articleEntity.getImg1(),
                articleEntity.getImg2(),
                articleEntity.getImg3(),
                articleEntity.getImg4(),
                articleEntity.getImg5()
        };

        // 获取项目根目录和上传目录
        String projectRoot = System.getProperty("user.dir");
        String fullUploadDir = projectRoot + "/" + this.contentDir;
        Path uploadPath = Paths.get(fullUploadDir);

        // 删除数据库记录
        boolean isDel = false;
        try {
            // 数据只删除一次
            isDel = this.removeById(id);
        } catch (MyBatisSystemException e) {
            // 记录根本原因
            Throwable rootCause = e.getRootCause();
            String errorMessage = "删除记录时发生错误: " + (rootCause != null ? rootCause.getMessage() : e.getMessage());
            return Result.failure(202, errorMessage);
        }

        // 删除图片文件
        StringBuilder deletedFiles = new StringBuilder();
        StringBuilder failedFiles = new StringBuilder();

        for (String img : imgPaths) {
            // 确保图片路径不为空
            if (img != null && !img.isEmpty()) {
                Path filePath = uploadPath.resolve(img);
                try {
                    boolean isDeleted = Files.deleteIfExists(filePath);
                    if (isDeleted) {
                        deletedFiles.append(img).append(", ");
                    } else {
                        failedFiles.append(img).append(", ");
                    }
                } catch (IOException e) {
                    failedFiles.append(img).append("(错误：").append(e.getMessage()).append("), ");
                }
            }
        }

        // 构造返回结果
        if (isDel) {
            String successMessage = "数据删除成功。";
            if (deletedFiles.length() > 0) {
                successMessage += " 文件删除成功: " + deletedFiles;
            }
            if (failedFiles.length() > 0) {
                successMessage += " 文件删除失败: " + failedFiles;
            }
            return Result.success(successMessage);
        } else {
            return Result.failure(202, "数据删除失败");
        }
    }

    @Override
    public Result<?> update(MultipartFile img1, MultipartFile img2,
                            MultipartFile img3, MultipartFile img4,
                            MultipartFile img5, ArticleEntity article) {
        String content = article.getContent();
        String regex2 = rege2;
        Pattern pattern = Pattern.compile(regex2);
        Matcher matcher = pattern.matcher(content);
        List<String> imgPaths = new ArrayList<>();

        while (matcher.find()) {
            String fileName = matcher.group(1);
            imgPaths.add(fileName);
        }

        ArticleEntity articleEntity = this.getOne(new QueryWrapper<ArticleEntity>().eq("ID", article.getId()));
        List<String> dbImagePaths = Arrays.asList(
                articleEntity.getImg1(),
                articleEntity.getImg2(),
                articleEntity.getImg3(),
                articleEntity.getImg4(),
                articleEntity.getImg5()
        );
        dbImagePaths = dbImagePaths.stream()
                .filter(Objects::nonNull)
                .filter(path -> !path.isEmpty())
                .map(String::trim)
                .collect(Collectors.toList());

        System.out.println("dbImagePaths: " + dbImagePaths);
        System.out.println("imgPaths: " + imgPaths);

// 修改过滤逻辑：找出 dbImagePaths 中不在 imgPaths 中的路径
        List<String> finalImgPaths = imgPaths;
        List<String> oldImagePathsToDelete = dbImagePaths.stream()
                .filter(dbPath -> {
                    boolean contains = finalImgPaths.contains(dbPath);
                    System.out.println("Checking: " + dbPath + " -> Contained in imgPaths: " + contains);
                    return !contains;
                })
                .collect(Collectors.toList());

        System.out.println("oldImagePathsToDelete: " + oldImagePathsToDelete);
        String projectRoot = System.getProperty("user.dir");
        String fullUploadDir = projectRoot + "/" + this.contentDir;
        Path uploadPath = Paths.get(fullUploadDir);

        for (String oldPath : oldImagePathsToDelete) {
            if (oldPath != null && !oldPath.isEmpty()) {
                Path filePath = uploadPath.resolve(oldPath);
                try {
                    Files.deleteIfExists(filePath);

                } catch (IOException e) {
                    return Result.failure(202, "文件删除失败：" + e.getMessage());
                }
            }
        }
        List<MultipartFile> images = Arrays.asList(img1, img2, img3, img4, img5);
        // 存储已上传的文件名
        List<String> uploadedFileNames = new ArrayList<>();

        // 遍历每个文件并处理上传逻辑
        for (MultipartFile img : images) {
            if (img != null && !img.isEmpty()) {
                String fileName = img.getOriginalFilename();
                Path targetLocation = uploadPath.resolve(fileName);

                // 检查文件是否已存在
                if (Files.exists(targetLocation)) {
                    // 回滚已上传的文件
                    rollbackUploadedFiles(uploadPath, uploadedFileNames);
                    return Result.failure(202, "文件已存在: " + fileName);
                }

                try (InputStream inputStream = img.getInputStream()) {
                    // 将输入流中的数据复制到目标位置
                    Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
                    // 记录已上传的文件名
                    uploadedFileNames.add(fileName);
                } catch (InterruptedIOException e) {
                    // 处理由于线程中断引起的IO异常
                    Thread.currentThread().interrupt();
                    rollbackUploadedFiles(uploadPath, uploadedFileNames);
                    return Result.failure(203, "文件上传被中断: " + fileName);
                } catch (IOException e) {
                    // 处理其他IO异常
                    rollbackUploadedFiles(uploadPath, uploadedFileNames);
                    return Result.failure(202, "无法保存上传该文件: " + fileName);
                }
            }
        }
        try {
            articleEntity.setContent(content);
            articleEntity.setSuggest(article.getSuggest());
            articleEntity.setIsShow(article.getIsShow());
            articleEntity.setTitle(article.getTitle());
            articleEntity.setType(article.getType());
            List<String> result = Stream.concat(uploadedFileNames.stream(), imgPaths.stream())
                    .collect(Collectors.toList());
            updateData(articleEntity, result);
            return Result.success("文章及图片保存成功");
        } catch (Exception e) {
            rollbackUploadedFiles(uploadPath, uploadedFileNames);
            return Result.failure(202, "文章数据保存失败: " + e.getMessage());
        }
    }

    private void updateData(ArticleEntity articleEntity, List<String> uploadedFileNames) {
        System.out.println("uploadedFileNames: " + uploadedFileNames);
        // 设置时间戳
        long date = System.currentTimeMillis() / 1000;
        articleEntity.setUpdated(date);

        // 获取原始 content
        String content = articleEntity.getContent();

        // 替换 content 中的图片链接
        if (content != null && !uploadedFileNames.isEmpty()) {
            content = replaceImageLinks(content, uploadedFileNames);
            articleEntity.setContent(content);
        }
        articleEntity.setImg1(null);
        articleEntity.setImg2(null);
        articleEntity.setImg3(null);
        articleEntity.setImg4(null);
        articleEntity.setImg5(null);
        // 检查 img1 到 img5 是否有空位，并分配新图片
        List<String> imgFields = Arrays.asList(
                articleEntity.getImg1(),
                articleEntity.getImg2(),
                articleEntity.getImg3(),
                articleEntity.getImg4(),
                articleEntity.getImg5()
        );

        // 找出第一个为空的字段索引
        int firstEmptyIndex = -1;
        for (int i = 0; i < imgFields.size(); i++) {
            if (imgFields.get(i) == null || imgFields.get(i).isEmpty()) {
                firstEmptyIndex = i;
                break;
            }
        }

        // 如果没有空位且上传了新图片，则抛出异常
        if (firstEmptyIndex == -1 && !uploadedFileNames.isEmpty()) {
            throw new RuntimeException("图片数量已达到上限，不能超过五张");
        }

        // 分配新图片到空位
        for (String uploadedFileName : uploadedFileNames) {
            if (firstEmptyIndex >= 0 && firstEmptyIndex < 5) {
                switch (firstEmptyIndex) {
                    case 0:
                        articleEntity.setImg1(uploadedFileName);
                        break;
                    case 1:
                        articleEntity.setImg2(uploadedFileName);
                        break;
                    case 2:
                        articleEntity.setImg3(uploadedFileName);
                        break;
                    case 3:
                        articleEntity.setImg4(uploadedFileName);
                        break;
                    case 4:
                        articleEntity.setImg5(uploadedFileName);
                        break;
                }
                firstEmptyIndex++; // 移动到下一个空位
            } else {
                throw new RuntimeException("图片数量已达到上限，不能超过五张");
            }
        }
        // 保存或更新数据
        System.out.println(articleEntity.getIsShow());
        System.out.println(articleEntity.getSuggest());

        boolean success = this.saveOrUpdate(articleEntity, new QueryWrapper<ArticleEntity>().eq("ID", articleEntity.getId()));
        if (!success) {
            throw new RuntimeException("文章数据保存失败");
        }
    }

    @Override
    public Result<?> listArticle(int type) {
        List<ArticleEntity> list = this.list(new QueryWrapper<ArticleEntity>().eq("type", type));
        return Result.success(list);
    }


    @Override
    public Result<?> newPub() {
        List<ArticleEntity> list = this.list(new QueryWrapper<ArticleEntity>()
                .select("id", "title")
                .eq("isShow", 1)
                .eq("type", 10)
                .orderByDesc("id")
                .last("LIMIT 4")
        );

        return Result.success(list);
    }

    @Override
    public Result<?> homeArt() {
        List<ArticleEntity> list1 = this.list(new QueryWrapper<ArticleEntity>()
                .select("id", "title", "created")
                .eq("isShow", 1)
                .eq("type", 10)

        );
        List<ArticleEntity> list2 = this.list(new QueryWrapper<ArticleEntity>()
                .select("id", "title", "created")
                .eq("isShow", 1)
                .eq("type", 11)
        );
        List<List<ArticleEntity>> resultList = new ArrayList<>();
        resultList.add(list1);
        resultList.add(list2);

        // 返回包含两个子列表的结果
        return Result.success(resultList);
    }

    @Override
    public Result<?> homeArt1() {
        List<ArticleEntity> list = this.list(new QueryWrapper<ArticleEntity>()
                .select("id", "title", "created", "img1")
                .eq("isShow", 1)
                .eq("type", 12)
        );
        List<List<ArticleEntity>> groupedData = new ArrayList<>();
        int size = list.size();
        for (int i = 0; i < size; i += 3) {
            groupedData.add(list.subList(i, Math.min(i + 3, size)));
        }
        return Result.success(groupedData);
    }

    @Override
    public Result<?> homeArt2() {
        List<ArticleEntity> list = this.list(new QueryWrapper<ArticleEntity>()
                .select("id", "title", "created", "img1")
                .eq("isShow", 1)
                .eq("type", 13)
        );
        List<List<ArticleEntity>> groupedData = new ArrayList<>();
        int size = list.size();
        for (int i = 0; i < size; i += 3) {
            groupedData.add(list.subList(i, Math.min(i + 3, size)));
        }

        return Result.success(groupedData);
    }
}
