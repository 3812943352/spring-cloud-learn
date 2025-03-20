/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-15 00:20:14
 * @FilePath: video-service/src/main/java/org/learning/videoservice/service/VideoService.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.videoservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.commonmodule.resp.Result;
import org.learning.videoservice.entity.VideoEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 王博
 * @since 2025-03-13
 */
public interface VideoService extends IService<VideoEntity> {


    Result<?> saveVideo(MultipartFile file, String category, String name,
                        int chunkNumber, int totalChunks, String fileHash);

}
