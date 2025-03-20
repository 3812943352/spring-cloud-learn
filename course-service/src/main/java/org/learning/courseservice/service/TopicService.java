/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 16:01:49
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-20 10:39:34
 * @FilePath: course-service/src/main/java/org/learning/courseservice/service/TopicService.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.service;

import com.common.commonmodule.resp.Result;
import org.learning.courseservice.entity.TopicEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 王博
 * @since 2025-03-14
 */
public interface TopicService extends IService<TopicEntity> {

    Result<?> addTopic(TopicEntity topicEntity);

    Result<?> updateTopic(TopicEntity topicEntity);

    Result<?> deleteTopic(Integer id);

    Result<?> get(int pageNum, int pageSize, int type);

    Result<?> blur(int pageNum, int pageSize, String word, String type);
}
