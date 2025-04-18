/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-08 07:25:14
 * @FilePath: course-service/src/main/java/org/learning/courseservice/service/CertService.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.commonmodule.resp.Result;
import org.learning.courseservice.entity.CertEntity;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 王博
 * @since 2025-03-13
 */
public interface CertService extends IService<CertEntity> {

    Result<?> get(int pageNum, int pageSize);

    Result<?> blur(int pageNum, int pageSize, String word);

    Result<?> date(int pageNum, int pageSize, long startTime, long endTime);


    Result<?> userCert(String phone);


    Result<?> certValid(String idNum, String phone, String cert);

    Boolean validateCaptcha(String captchaKey, String userInput);
}
