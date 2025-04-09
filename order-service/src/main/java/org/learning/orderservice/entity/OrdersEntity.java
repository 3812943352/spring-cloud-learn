/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-03 11:44:31
 * @FilePath: order-service/src/main/java/org/learning/orderservice/entity/OrdersEntity.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.orderservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author 王博
 * @since 2025-03-13
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("orders")
@Schema(name = "OrdersEntity", description = "$!{table.comment}")
public class OrdersEntity extends Model<OrdersEntity> {

    @Schema(description = "订单ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "创建时间")
    @TableField("created")
    private Long created;

    @Schema(description = "下单用户ID")
    @TableField("user")
    private Integer user;

    @Schema(description = "下单课程ID")
    @TableField("course")
    private Integer course;

    @Schema(description = "订单状态：0未支付 1已支付")
    @TableField("status")
    private Integer status;

    @Schema(description = "订单价格")
    @TableField("price")
    private Double price;

    @Schema(description = "订单唯一ID")
    @TableField("orderId")
    private String orderId;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
