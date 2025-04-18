/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 14:16:07
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-13 14:58:23
 * @FilePath: ApiSupervision-service/src/main/java/org/learning/apisupervisionservice/service/IpService.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.apisupervisionservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.commonmodule.resp.Result;
import org.learning.apisupervisionservice.entity.IpEntity;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 王博
 * @since 2025-03-13
 */
public interface IpService extends IService<IpEntity> {

    Result<?> getBan(int pageNum, int pageSize);


    Result<?> setBan(String ip, String reason, Long time);

    Result<?> unBan(String ip);

    Result<?> updateBan(String ip, String reason, Long time);

    Result<?> getBlur(int pageNum, int pageSize, String word);

    Result<?> getDate(int pageNum, int pageSize, long startTime, long endTime);
}