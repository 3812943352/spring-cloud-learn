/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 15:56:22
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-26 21:42:50
 * @FilePath: contents-service/src/main/java/org/learning/contentsservice/service/impl/SettingServiceImpl.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.contentsservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.commonmodule.resp.Result;
import org.learning.contentsservice.entity.SettingEntity;
import org.learning.contentsservice.mapper.SettingMapper;
import org.learning.contentsservice.service.SettingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 王博
 * @since 2025-03-14
 */
@Service
public class SettingServiceImpl extends ServiceImpl<SettingMapper, SettingEntity> implements SettingService {
    @Value("${file.content-dir}")
    private String contentDir;

    @Override
    public Result<?> updateSetting(MultipartFile img1, MultipartFile img2,
                                   MultipartFile img3, MultipartFile img4,
                                   MultipartFile img5) {
        SettingEntity settingEntity = this.getOne(new QueryWrapper<SettingEntity>().eq("id", 1));
        try {
            if (img1 != null && !img1.isEmpty()) {
                String img1Name = img1.getOriginalFilename();
                settingEntity.setImg1(img1Name);
                this.delFile(settingEntity.getImg1());
                this.saveFile(img1, img1Name);
            }
            if (img2 != null && !img2.isEmpty()) {
                String img2Name = img2.getOriginalFilename();
                settingEntity.setImg2(img2Name);
                this.delFile(settingEntity.getImg2());
                this.saveFile(img2, img2Name);
            }
            if (img3 != null && !img3.isEmpty()) {
                String img3Name = img3.getOriginalFilename();
                settingEntity.setImg3(img3Name);
                this.delFile(settingEntity.getImg3());
                this.saveFile(img3, img3Name);
            }
            if (img4 != null && !img4.isEmpty()) {
                String img4Name = img4.getOriginalFilename();
                settingEntity.setImg4(img4Name);
                this.delFile(settingEntity.getImg4());
                this.saveFile(img4, img4Name);
            }
            if (img5 != null && !img5.isEmpty()) {
                String img5Name = img5.getOriginalFilename();
                settingEntity.setImg5(img5Name);
                this.delFile(settingEntity.getImg5());
                this.saveFile(img5, img5Name);
            }
            this.saveData(settingEntity);
            return Result.success("更新成功");
        } catch (Exception e) {
            return Result.failure("更新失败" + e.getMessage());
        }


    }

    public void delFile(String fileName) {
        String projectRoot = System.getProperty("user.dir");
        String fullUploadDir = projectRoot + "/" + this.contentDir + "/setting";
        Path uploadPath = Paths.get(fullUploadDir);
        System.out.println(fullUploadDir);

        Path filePath = uploadPath.resolve(fileName);
        try {
            Files.deleteIfExists(filePath);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveFile(MultipartFile file, String name) {
        String projectRoot = System.getProperty("user.dir");
        String fullUploadDir = projectRoot + "/" + this.contentDir + "/setting";
        System.out.println(fullUploadDir);
        Path uploadPath = Paths.get(fullUploadDir);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                Result.failure(e.getMessage());
                return;
            }
        }
        Path targetLocation = uploadPath.resolve(name);
        if (Files.exists(targetLocation)) {
            Result.failure(202, "文件已存在: " + name);
            return;
        }
        try (InputStream inputStream = file.getInputStream()) {
            // 将输入流中的数据复制到目标位置
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (InterruptedIOException e) {
            // 处理由于线程中断引起的IO异常
            Thread.currentThread().interrupt(); // 重新设置中断状态
            try {
                Files.deleteIfExists(targetLocation);
            } catch (IOException deleteException) {
                Result.failure(204, "无法删除的不完整文件: " + name + ", 原因: " + deleteException.getMessage());
                return;
            }
            Result.failure(203, "文件上传被中断: " + name);
            return;
        } catch (IOException e) {
            // 处理其他IO异常
            try {
                Files.deleteIfExists(targetLocation);
            } catch (IOException deleteException) {
                Result.failure(204, "无法删除不完整的文件: " + name + ", 原因: " + deleteException.getMessage());
                return;
            }
            Result.failure(202, "无法保存上传该文件: " + name);
            return;
        }
        Result.success();
    }

    public void saveData(SettingEntity settingEntity) {
        this.saveOrUpdate(settingEntity, new QueryWrapper<SettingEntity>().eq("id", 1));
    }

    @Override
    public Result<?> get(int id) {
        SettingEntity settingEntity = this.getOne(new QueryWrapper<SettingEntity>().eq("id", id));
        return Result.success(settingEntity);
    }
}
