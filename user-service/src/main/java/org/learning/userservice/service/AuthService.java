/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 09:55:46
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-14 11:05:40
 * @FilePath: user-service/src/main/java/org/learning/userservice/service/AuthService.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.userservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.commonmodule.resp.Result;
import org.learning.userservice.entity.AuthEntity;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 王博
 * @since 2025-03-13
 */
public interface AuthService extends IService<AuthEntity> {
    Result<?> getAuth(int pageNum, int pageSize);

    Result<?> addAuth(AuthEntity authEntity);

    Result<?> delAuth(AuthEntity authEntity);

    Result<?> updateAuth(AuthEntity authEntity);
}
