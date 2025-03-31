/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-29 19:28:48
 * @FilePath: course-service/src/main/java/org/learning/courseservice/service/ExpertService.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.commonmodule.resp.Result;
import lombok.SneakyThrows;
import org.learning.courseservice.entity.ExpertEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 王博
 * @since 2025-03-13
 */
public interface ExpertService extends IService<ExpertEntity> {


    @SneakyThrows
    Result<?> add(ExpertEntity expertEntity, String name, MultipartFile file);


    Result<?> update(MultipartFile file, String name, ExpertEntity expertEntity);

    Result<?> get(int pageNum, int pageSize);

    Result<?> del(int iD);

    Result<?> blur(int pageNum, int pageSize, String word);

    Result<?> listEx();

    Result<?> date(int pageNum, int pageSize, long startTime, long endTime);
}
