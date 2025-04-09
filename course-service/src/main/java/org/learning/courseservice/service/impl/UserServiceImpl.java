/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 09:50:02
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-08 05:16:58
 * @FilePath: course-service/src/main/java/org/learning/courseservice/service/impl/UserServiceImpl.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.learning.courseservice.entity.UserEntity;
import org.learning.courseservice.mapper.UserMapper;
import org.learning.courseservice.service.UserService;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 王博
 * @since 2025-03-13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {


}