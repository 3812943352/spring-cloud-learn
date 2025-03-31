/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 15:56:28
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-30 13:21:00
 * @FilePath: contents-service/src/main/java/org/learning/contentsservice/service/SettingService.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.contentsservice.service;

import com.common.commonmodule.resp.Result;
import org.learning.contentsservice.entity.SettingEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 王博
 * @since 2025-03-14
 */
public interface SettingService extends IService<SettingEntity> {


    Result<?> updateSetting(MultipartFile img1, MultipartFile img2,
                            MultipartFile img3, MultipartFile img4,
                            MultipartFile img5, String phone, String email, String num, String cr);

    Result<?> get(int id);
}
