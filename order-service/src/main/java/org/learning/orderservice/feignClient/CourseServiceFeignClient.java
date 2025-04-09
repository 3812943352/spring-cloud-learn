/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-04-03 13:12:32
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-07 21:51:14
 * @FilePath: order-service/src/main/java/org/learning/orderservice/feignClient/CourseServiceFeignClient.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.orderservice.feignClient;

import com.common.commonmodule.resp.Result;
import org.learning.orderservice.entity.CourseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(value = "course-service")
public interface CourseServiceFeignClient {

    @PostMapping(value = "/course/coursePrice")
    Result<?> coursePrice(@RequestParam("id") int id);


    @PostMapping(value = "/course/getCourseList")
    Result<?> getCourseList(List<Integer> courseList);


    @PostMapping(value = "/course/getCourseById")
    Result<?> getCourseById(@RequestParam("id") int id);
}

