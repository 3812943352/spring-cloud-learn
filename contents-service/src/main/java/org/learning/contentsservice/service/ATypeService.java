/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 09:01:36
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-24 16:04:40
 * @FilePath: contents-service/src/main/java/org/learning/contentsservice/service/ATypeService.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.contentsservice.service;

import com.common.commonmodule.resp.Result;
import org.learning.contentsservice.entity.ATypeEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 王博
 * @since 2025-03-14
 */
public interface ATypeService extends IService<ATypeEntity> {

    Result<?> addType(ATypeEntity aTypeEntity);

    Result<?> updateType(ATypeEntity aTypeEntity);

    Result<?> deleteType(Integer id);

    Result<?> getType(Integer pageNum, Integer pageSize);

    Result<?> listType();
}
