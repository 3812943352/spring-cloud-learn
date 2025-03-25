/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-23 20:13:19
 * @FilePath: course-service/src/main/java/org/learning/courseservice/service/CTypeService.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.commonmodule.resp.Result;
import org.learning.courseservice.entity.CTypeEntity;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 王博
 * @since 2025-03-13
 */
public interface CTypeService extends IService<CTypeEntity> {

    Result<?> addType(CTypeEntity cTypeEntity);

    Result<?> updateType(CTypeEntity cTypeEntity);

    Result<?> deleteType(Integer id);


    Result<?> getType(Integer pageNum, Integer pageSize);

    Result<?> listType();
}
