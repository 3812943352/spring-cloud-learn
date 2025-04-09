/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-04-03 01:37:20
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-08 03:22:53
 * @FilePath: video-service/src/main/java/org/learning/videoservice/service/ProgressService.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.videoservice.service;

import com.common.commonmodule.resp.Result;
import org.learning.videoservice.entity.ProgressEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 王博
 * @since 2025-04-03
 */
public interface ProgressService extends IService<ProgressEntity> {

    Result<?> addOrUpdateProgress(ProgressEntity progressEntity);

    Result<?> userVideoRecord(int user);

    Result<?> videoProgress(int user, int video);

    Result<?> isDone(int user, int course);

    Result<?> courseProgress(int user, int course);
}
