/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 16:01:49
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-07 02:02:38
 * @FilePath: course-service/src/main/java/org/learning/courseservice/service/TopicService.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.service;

import com.common.commonmodule.resp.Result;
import org.learning.courseservice.entity.Choice;
import org.learning.courseservice.entity.Judge;
import org.learning.courseservice.entity.Radio;
import org.learning.courseservice.entity.TopicEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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


    Result<?> scoring(List<Radio> radioAnswer, List<Choice> choiceAnswer, List<Judge> judgeAnswer, int course, String phone, int user);
}
