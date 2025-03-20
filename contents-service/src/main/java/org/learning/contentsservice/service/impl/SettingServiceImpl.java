/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 15:56:22
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-14 15:57:37
 * @FilePath: contents-service/src/main/java/org/learning/contentsservice/service/impl/SettingServiceImpl.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.contentsservice.service.impl;

import org.learning.contentsservice.entity.SettingEntity;
import org.learning.contentsservice.mapper.SettingMapper;
import org.learning.contentsservice.service.SettingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 王博
 * @since 2025-03-14
 */
@Service
public class SettingServiceImpl extends ServiceImpl<SettingMapper, SettingEntity> implements SettingService {

}
