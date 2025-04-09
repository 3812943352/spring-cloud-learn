/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-07 12:03:53
 * @FilePath: course-service/src/main/java/org/learning/courseservice/service/CourseService.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.commonmodule.resp.Result;
import lombok.SneakyThrows;
import org.learning.courseservice.entity.CourseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 王博
 * @since 2025-03-13
 */
public interface CourseService extends IService<CourseEntity> {


    @SneakyThrows
    Result<?> saveFile(MultipartFile file, String name, CourseEntity courseEntity);

    Result<?> get(int pageNum, int pageSize);

    Result<?> update(MultipartFile file, String name, CourseEntity courseEntity);

    Result<?> del(int iD);

    Result<?> blur(int pageNum, int pageSize, String word);

    Result<?> date(int pageNum, int pageSize, long startTime, long endTime);


    Result<?> listCourse();


    Result<?> getCourseByType(int pageNum, int pageSize, int type);

    Result<?> getCourseById(int id);

    Result<?> userHome();

    Result<?> coursePrice(int id);

    Result<?> videoList(int id);

    Result<?> getCourseList(List<Integer> courseList);
}
