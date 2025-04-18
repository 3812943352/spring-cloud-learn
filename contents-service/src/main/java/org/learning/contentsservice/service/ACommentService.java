/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 09:01:17
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-26 10:40:21
 * @FilePath: contents-service/src/main/java/org/learning/contentsservice/service/ACommentService.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.contentsservice.service;

import com.common.commonmodule.resp.Result;
import org.learning.contentsservice.entity.ACommentEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 王博
 * @since 2025-03-14
 */
public interface ACommentService extends IService<ACommentEntity> {

    Result<?> add(ACommentEntity aCommentEntity);

    Result<?> update(ACommentEntity aCommentEntity);

    Result<?> del(int id);

    Result<?> get(int pageNum, int pageSize);

    Result<?> blur(int pageNum, int pageSize, String word);

    Result<?> date(int pageNum, int pageSize, long startTime, long endTime);
}
