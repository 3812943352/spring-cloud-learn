package org.learning.videoservice.mapper; /**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-14 09:17:11
 * @FilePath: video-service/src/main/java/org/learning/videoservice/mapper/VideoMapper.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.learning.videoservice.entity.VideoEntity;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 王博
 * @since 2025-03-13
 */
@Mapper
public interface VideoMapper extends BaseMapper<VideoEntity> {

}
