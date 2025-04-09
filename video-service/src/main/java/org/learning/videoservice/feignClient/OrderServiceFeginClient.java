/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-04-03 15:39:03
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-03 18:04:02
 * @FilePath: video-service/src/main/java/org/learning/videoservice/feignClient/OrderServiceFeginClient.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.videoservice.feignClient;

import com.common.commonmodule.resp.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "order-service")
public interface OrderServiceFeginClient {

    @PostMapping(value = "/order/isBuy")
    Result<?> isBuy(@RequestParam("user") Integer userId, @RequestParam("course") Integer courseId);


}
