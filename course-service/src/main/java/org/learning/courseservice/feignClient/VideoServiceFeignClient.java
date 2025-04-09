/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-04-05 13:33:26
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-05 14:28:06
 * @FilePath: course-service/src/main/java/org/learning/courseservice/feignClient/VideoServiceFeignClient.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.feignClient;

import com.common.commonmodule.resp.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "video-service")
public interface VideoServiceFeignClient {

    @PostMapping(value = "/video/isDone")
    Result<?> isDone(@RequestParam("user") int user, @RequestParam("course") int course);
}
