/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 16:38:17
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-27 20:38:23
 * @FilePath: course-service/src/main/java/org/learning/courseservice/service/TemplateService.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.service;

import com.common.commonmodule.resp.Result;
import lombok.SneakyThrows;
import org.learning.courseservice.entity.TemplateEntity;
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
public interface TemplateService extends IService<TemplateEntity> {

    @SneakyThrows
    Result<?> add(TemplateEntity templateEntity, String name, MultipartFile file);

    Result<?> update(MultipartFile file, String name, TemplateEntity templateEntity);

    Result<?> get(int pageNum, int pageSize);

    Result<?> del(int iD);

    Result<?> blur(int pageNum, int pageSize, String word);
}
