/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 16:01:49
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-07 02:13:56
 * @FilePath: course-service/src/main/java/org/learning/courseservice/service/PaperService.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.service;

import com.common.commonmodule.resp.Result;
import org.learning.courseservice.entity.Choice;
import org.learning.courseservice.entity.Judge;
import org.learning.courseservice.entity.PaperEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.learning.courseservice.entity.Radio;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 王博
 * @since 2025-03-14
 */
public interface PaperService extends IService<PaperEntity> {

    Result<?> addPaper(PaperEntity paperEntity);

    Result<?> updatePaper(PaperEntity paperEntity);


    Result<?> delPaper(int id, int course);

    Result<?> get(int pageNum, int pageSize);

    Result<?> blur(int pageNum, int pageSize, String word);

    Result<?> date(int pageNum, int pageSize, long startTime, long endTime);

    Result<?> listCourse();


    Result<?> getTopic(int user, int course, String phone);
}
