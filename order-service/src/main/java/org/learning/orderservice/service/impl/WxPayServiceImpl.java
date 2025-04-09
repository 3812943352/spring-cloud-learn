/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-04-02 19:47:39
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-04 12:37:03
 * @FilePath: order-service/src/main/java/org/learning/orderservice/service/impl/WxPayServiceImpl.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.orderservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.commonmodule.resp.Result;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
import org.learning.orderservice.entity.OrdersEntity;
import org.learning.orderservice.feignClient.CourseServiceFeignClient;
import org.learning.orderservice.mapper.OrdersMapper;
import org.learning.orderservice.service.WxPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WxPayServiceImpl implements WxPayService {

    private final NotificationParser notificationParser;
    @Autowired
    private NativePayService nativePayService;
    @Value("${wechat.appId}")
    private String appId;
    @Value("${wechat.mchId}")
    private String mchId;
    @Value("${wechat.notifyUrl}")
    private String notifyUrl;

    @Autowired
    private OrdersMapper orderMapper;
    @Autowired
    private CourseServiceFeignClient courseServiceFeignClient;

    public WxPayServiceImpl(NotificationParser notificationParser) {
        this.notificationParser = notificationParser;
    }

    /**
     * 统一下单，生成支付二维码
     */
    @Override
    public Result<?> createPayment(Integer userId, Integer courseId, String orderId, String des, Long date) {
        try {
            OrdersEntity ordersEntity = orderMapper.selectOne(new QueryWrapper<OrdersEntity>()
                    .eq("user", userId)
                    .eq("course", courseId)
                    .eq("status", 1)
            );
            if (ordersEntity != null) {
                return Result.success(null, "您已购买过该课程，请刷新页面", 201);
            }
            Result<?> coursePriceResult = courseServiceFeignClient.coursePrice(courseId);
            Double price = (Double) coursePriceResult.getData();
            PrepayRequest request = new PrepayRequest();
            int amount = (int) (price * 100);
            request.setAppid(appId);
            request.setMchid(mchId);
            request.setDescription(des);
            request.setOutTradeNo(orderId);
            request.setNotifyUrl(notifyUrl);

            Amount payAmount = new Amount();
            payAmount.setTotal(amount);
            request.setAmount(payAmount);

            PrepayResponse response = nativePayService.prepay(request);

            OrdersEntity orderEntity = new OrdersEntity();
            orderEntity.setUser(userId);
            orderEntity.setCourse(courseId);
            orderEntity.setStatus(0);
            orderEntity.setCreated(date);
            orderEntity.setOrderId(orderId);
            orderEntity.setPrice(price);
            this.orderMapper.insert(orderEntity);
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("orderId", orderId);
            resultData.put("codeUrl", response.getCodeUrl());
            return Result.success(resultData);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }

    }

    @Override
    public String handleNotify(String body, String signature, String timestamp, String nonce, String wechatpaySerial) {
        var requestParam = new RequestParam.Builder()
                .serialNumber(wechatpaySerial)
                .nonce(nonce)
                .signature(signature)
                .timestamp(timestamp)
                .body(body)
                .build();

        try {
            System.out.println(requestParam);
            // 解析通知内容
            Map<String, Object> decryptedData = notificationParser.parse(requestParam, Map.class);
            System.out.println("支付通知内容：" + decryptedData);

            // 提取关键信息
            String outTradeNo = (String) decryptedData.get("out_trade_no");
            String tradeState = (String) decryptedData.get("trade_state");

            // 处理订单
            processOrder(outTradeNo, tradeState);

            // 返回成功响应
            return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
        } catch (Exception e) {
            e.printStackTrace();
            return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[ERROR]]></return_msg></xml>";
        }
    }

    /**
     * 处理订单逻辑
     */
    private void processOrder(String outTradeNo, String tradeState) throws Exception {
        if (!"SUCCESS".equals(tradeState)) {
            throw new Exception("支付状态异常：" + tradeState);
        }

        // 查询订单是否存在
        OrdersEntity order = orderMapper.selectOne(new QueryWrapper<OrdersEntity>().eq("orderId", outTradeNo));
        if (order == null) {
            throw new Exception("订单不存在：" + outTradeNo);
        }


        // 检查订单是否已被处理
        if (order.getStatus() == 1) {
            System.out.println("订单 " + outTradeNo + " 已经处理过，无需重复更新");
            return;
        }

        // 更新订单状态为已支付
        order.setStatus(1);
        orderMapper.updateById(order);

        System.out.println("订单 " + outTradeNo + " 已支付，状态已更新");
        Integer userId = order.getUser();
        Integer courseId = order.getCourse();
        this.orderMapper.delete(new QueryWrapper<OrdersEntity>()
                .eq("user", userId)
                .eq("course", courseId)
                .eq("status", 0));
    }
}
