/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-12 16:53:42
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-13 16:25:42
 * @FilePath: ApiSupervision-service/src/main/java/org/learning/apisupervisionservice/service/ApiService.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.apisupervisionservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.commonmodule.resp.Result;
import org.learning.apisupervisionservice.entity.ApiEntity;


/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 王博
 * @since 2025-03-12
 */
public interface ApiService extends IService<ApiEntity> {

    Result<?> homeData();

    Result<?> getLine();

    Result<?> getCharts(long start, long end);

    Result<?> addApi(ApiEntity apiEntity);

    Result<?> deleteApi(ApiEntity apiEntity);

    Result<?> updateApi(ApiEntity apiEntity);

    Result<?> getAllApi(int pageNum, int pageSize);

    Result<?> getApiById(Integer id);

    Result<?> getBlur(int pageNum, int pageSize, String word);
}

