/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 09:01:55
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-27 13:35:49
 * @FilePath: contents-service/src/main/java/org/learning/contentsservice/service/ArticleService.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.contentsservice.service;

import com.common.commonmodule.resp.Result;
import org.learning.contentsservice.entity.ArticleEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 王博
 * @since 2025-03-14
 */
public interface ArticleService extends IService<ArticleEntity> {

    Result<?> addArticle(MultipartFile img1, MultipartFile img2,
                         MultipartFile img3, MultipartFile img4,
                         MultipartFile img5, ArticleEntity articleEntity);

    Result<?> getArticle(int pageNum, int pageSize);

    Result<?> blur(int pageNum, int pageSize, String word);

    Result<?> date(int pageNum, int pageSize, long startTime, long endTime);

    Result<?> del(int id);

    Result<?> update(MultipartFile img1, MultipartFile img2,
                     MultipartFile img3, MultipartFile img4,
                     MultipartFile img5, ArticleEntity articleEntity);

    Result<?> listArticle(int type);
}
