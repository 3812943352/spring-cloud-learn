/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 09:50:07
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-31 11:30:56
 * @FilePath: user-service/src/main/java/org/learning/userservice/service/UserService.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.userservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.common.commonmodule.resp.Result;
import org.learning.userservice.entity.UserEntity;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 王博
 * @since 2025-03-13
 */
public interface UserService extends IService<UserEntity> {

    Result<?> userBlur(int pageNum, int pageSize, String word);

    Result<?> Database();

    UserEntity getUserByPhone(String phone);


    UserEntity getUserById(int ID);

    Page<UserEntity> getAllUserPagination(int pageNum, int pageSize);


    Result<?> DelUserById(UserEntity userEntity);

    Result<?> updateUser(UserEntity userEntity);

    Result<?> resetPwd(String phone, String pwd, String oldPwd);

    Result<?> resetPhone(String oldPhone, String newPhone, String pwd);

    Result<?> login(String phone);

    Result<?> register(UserEntity userEntity);
}

