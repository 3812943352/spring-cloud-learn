/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-04-04 13:49:27
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-04 13:50:23
 * @FilePath: video-service/src/main/java/org/learning/videoservice/feignClient/CourseServiceFeignClient.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.videoservice.feignClient;

import com.common.commonmodule.resp.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "course-service")
public interface CourseServiceFeignClient {

    @PostMapping(value = "/course/videoList")
    Result<?> videoList(@RequestParam("id") Integer id);
}
