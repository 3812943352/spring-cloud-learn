/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-09 11:51:29
 * @FilePath: order-service/src/main/java/org/learning/orderservice/service/impl/OrdersServiceImpl.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.orderservice.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.commonmodule.resp.Result;
import org.learning.orderservice.entity.OrdersEntity;
import org.learning.orderservice.feignClient.CourseServiceFeignClient;
import org.learning.orderservice.mapper.OrdersMapper;
import org.learning.orderservice.service.OrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 王博
 * @since 2025-03-13
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, OrdersEntity> implements OrdersService {
    private final OrdersMapper ordersMapper;
    @Autowired
    private CourseServiceFeignClient courseServiceFeignClient;

    public OrdersServiceImpl(OrdersMapper ordersMapper) {
        this.ordersMapper = ordersMapper;
    }

    @Override
    public Result<?> getSta(String orderId) {
        OrdersEntity orderEntity = this.getOne(new QueryWrapper<OrdersEntity>().eq("orderId", orderId));
        if (orderEntity == null) {
            return Result.failure(202, "订单不存在");
        }
        if (orderEntity.getStatus() == 0) {
            return Result.failure(201, "订单未支付");
        }
        return Result.success(orderEntity.getStatus(), "支付成功");

    }

    @Override
    public Result<?> isBuy(Integer userId, Integer courseId) {

        OrdersEntity orderEntity = this.getOne(new QueryWrapper<OrdersEntity>()
                .eq("user", userId)
                .eq("course", courseId)
                .eq("status", 1));

        if (orderEntity == null) {
            return Result.success(false, null, 201);
        }

        return Result.success(true, "已购买");
    }


    @Override
    public Result<?> hasBuy(int user) {
        // 查询订单表中用户ID相同且状态为1（已支付）的订单
        List<OrdersEntity> orders = ordersMapper.selectList(
                new QueryWrapper<OrdersEntity>()
                        .eq("user", user)
                        .eq("status", 1)
        );

        // 提取订单中的课程ID，组成一个List<Integer>
        List<Integer> courseIds = orders.stream()
                .map(OrdersEntity::getCourse)
                .distinct()
                .collect(Collectors.toList());

        // 调用远程服务获取课程列表
        return courseServiceFeignClient.getCourseList(courseIds);
    }

    @Override
    public Result<?> freeStudy(int user, int course) {
        try {
            System.out.println(course);
            Result<?> coursePriceResult = courseServiceFeignClient.coursePrice(course);
            Double price = (Double) coursePriceResult.getData();
            double epsilon = 1e-6;
            if (Math.abs(price) < epsilon) {
                OrdersEntity ordersEntity = new OrdersEntity();
                ordersEntity.setCourse(course);
                ordersEntity.setPrice(price);
                ordersEntity.setStatus(1);
                ordersEntity.setUser(user);
                ordersEntity.setCreated(System.currentTimeMillis() / 1000);
                long date = System.currentTimeMillis() / 1000;
                String orderId = date + RandomUtil.randomNumbers(8);
                ordersEntity.setOrderId(orderId);
                this.save(ordersEntity);
            }
            return Result.success();
        } catch (Exception e) {
            return Result.failure(202, e.getMessage());
        }
    }


    @Override
    public Result<?> update(OrdersEntity ordersEntity) {
        return Result.success(ordersMapper.updateById(ordersEntity));
    }

    @Override
    public Result<?> get(int pageNum, int pageSize) {
        try {
            Page<OrdersEntity> resultPage = new Page<>(pageNum, pageSize);
            QueryWrapper<OrdersEntity> queryWrapper = new QueryWrapper<>();
            resultPage = this.page(resultPage, queryWrapper);
            return Result.success(resultPage);
        } catch (Exception e) {
            return Result.failure(202, "查询失败" + e);
        }
    }

    @Override
    public Result<?> blur(int pageNum, int pageSize, String word) {
        Page<OrdersEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<OrdersEntity> queryWrapper = new QueryWrapper<>();
        if (word != null && !word.isEmpty()) {
            queryWrapper.or(wrapper -> wrapper
                    .like("orderId", word)


            );
        }
        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage, "请求成功");
    }

    @Override
    public Result<?> date(int pageNum, int pageSize, long startTime, long endTime) {
        Page<OrdersEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<OrdersEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("created", startTime, endTime);
        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage);
    }

    @Override
    public Result<?> orderCount() {
        // 查询条件（可以为空，表示查询所有订单）
        QueryWrapper<OrdersEntity> queryWrapper = new QueryWrapper<>();

        // 统计订单数量
        int orderCount = Math.toIntExact(this.count(queryWrapper));

        // 计算订单总金额
        Double totalAmount = this.baseMapper.selectTotalPrice();

        // 封装结果到 Map 中
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("totalAmount", totalAmount);
        resultMap.put("orderCount", orderCount);

        // 返回结果
        return Result.success(resultMap);
    }
    
}
