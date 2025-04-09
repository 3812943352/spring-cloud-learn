/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-09 11:51:29
 * @FilePath: order-service/src/main/java/org/learning/orderservice/service/OrdersService.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.orderservice.service;

import com.common.commonmodule.resp.Result;
import org.learning.orderservice.entity.OrdersEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 王博
 * @since 2025-03-13
 */
public interface OrdersService extends IService<OrdersEntity> {


    Result<?> getSta(String orderId);

    Result<?> isBuy(Integer userId, Integer courseId);

    Result<?> hasBuy(int user);

    Result<?> freeStudy(int user, int course);

    Result<?> update(OrdersEntity ordersEntity);

    Result<?> get(int pageNum, int pageSize);

    Result<?> blur(int pageNum, int pageSize, String word);

    Result<?> date(int pageNum, int pageSize, long startTime, long endTime);

    Result<?> orderCount();
}
