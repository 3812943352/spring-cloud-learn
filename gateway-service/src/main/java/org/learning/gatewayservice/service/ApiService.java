/**
 * @Author: wangbo 3812943352@qq.com
 * @Date: 2024-11-11 14:20:52
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-13 15:07:33
 * @FilePath: gateway-service/src/main/java/org/learning/gatewayservice/service/ApiService.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.gatewayservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.learning.gatewayservice.entity.ApiEntity;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 王博
 * @since 2024-11-09
 */
public interface ApiService extends IService<ApiEntity> {


    List<String> getApi();

    boolean addVisit(String api);

    boolean addTimes(String api);

    boolean addError(String api);


    boolean apiUserList(String api, String token);

    boolean apiShouldNot(String api);
}
