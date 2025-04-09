/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-04-02 20:06:28
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-04 11:34:53
 * @FilePath: order-service/src/main/java/org/learning/orderservice/service/WxPayService.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.orderservice.service;

import com.common.commonmodule.resp.Result;

public interface WxPayService {


    Result<?> createPayment(Integer userId, Integer courseId, String orderId, String des, Long date);


    String handleNotify(String body, String signature, String timestamp, String nonce, String wechatpaySerial);
}
