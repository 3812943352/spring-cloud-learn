/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-12 17:13:12
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-12 17:14:04
 * @FilePath: ApiSupervision-service/src/main/java/org/learning/apisupervisionservice/feiginClient/UserServiceFeignClient.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.apisupervisionservice.feiginClient;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "user-service")
public interface UserServiceFeignClient {
}
