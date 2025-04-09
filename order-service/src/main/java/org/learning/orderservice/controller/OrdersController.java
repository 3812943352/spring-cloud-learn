/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-09 13:00:39
 * @FilePath: order-service/src/main/java/org/learning/orderservice/controller/OrdersController.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.orderservice.controller;

import cn.hutool.core.util.RandomUtil;
import com.common.commonmodule.resp.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.learning.orderservice.entity.OrdersEntity;
import org.learning.orderservice.service.OrdersService;
import org.learning.orderservice.service.WxPayService;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 王博
 * @since 2025-03-13
 */
@RestController
@RequestMapping("/order")
public class OrdersController {
    private final WxPayService wxPayService;
    private final OrdersService orderService;

    public OrdersController(WxPayService wxPayService, OrdersService orderService) {
        this.wxPayService = wxPayService;
        this.orderService = orderService;
    }


    @Operation(summary = "支付二维码")
    @PostMapping(value = "/QrCode")
    public Result<?> qrCode(@RequestParam(value = "des") String des,
                            @RequestParam(value = "userId") Integer userId, @RequestParam(value = "courseId") Integer courseId) {

        long date = System.currentTimeMillis() / 1000;

        String orderId = date + RandomUtil.randomNumbers(8);
        return this.wxPayService.createPayment(userId, courseId, orderId, des, date);
    }


    @Operation(summary = "微信支付异步通知")
    @PostMapping("/notify")
    public String handleNotify(@RequestBody String body,
                               @RequestHeader("Wechatpay-Signature") String signature,
                               @RequestHeader("Wechatpay-Timestamp") String timestamp,
                               @RequestHeader("Wechatpay-Nonce") String nonce, @RequestHeader("Wechatpay-Serial") String wechatpaySerial) {
        return this.wxPayService.handleNotify(body, signature, timestamp, nonce, wechatpaySerial);
    }

    @Operation(summary = "订单状态查询")
    @PostMapping("/getSta")
    public Result<?> getSta(@RequestParam(value = "orderId") String orderId) {
        return this.orderService.getSta(orderId);
    }

    @Operation(summary = "是否已购买")
    @PostMapping("/isBuy")
    public Result<?> isBuy(@RequestParam(value = "user") Integer user, @RequestParam(value = "course") Integer course) {
        return this.orderService.isBuy(user, course);
    }

    @Operation(summary = "是否已购买")
    @PostMapping("/hasBuy")
    public Result<?> hasBuy(@RequestParam(value = "user") int user) {
        return this.orderService.hasBuy(user);
    }


    @Operation(summary = "免费学习")
    @PostMapping("/freeStudy")
    public Result<?> freeStudy(@RequestParam(value = "user") Integer user, @RequestParam(value = "course") Integer course) {
        return this.orderService.freeStudy(user, course);
    }


    @Operation(summary = "订单列表")
    @PostMapping(value = "/getOrder")
    public Result<?> getOrder(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
        return this.orderService.get(pageNum, pageSize);
    }

    @Operation(summary = "模糊查询订单")
    @PostMapping(value = "/blurOrder")
    public Result<?> blurOrder(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                               @RequestParam("word") String word) {
        return this.orderService.blur(pageNum, pageSize, word);
    }


    @Operation(summary = "根据时间查询订单")
    @PostMapping(value = "/updateOrder")
    public Result<?> updateOrder(@RequestBody OrdersEntity ordersEntity) {
        return this.orderService.update(ordersEntity);
    }

    @Operation(summary = "订单总和")
    @PostMapping(value = "/orderCount")
    public Result<?> orderCount() {
        return this.orderService.orderCount();
    }
}
