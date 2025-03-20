/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-14 09:20:15
 * @FilePath: order-service/src/main/java/org/learning/orderservice/entity/OrderEntity.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.orderservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.math.BigDecimal;

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
@TableName("order")
@Schema(name = "OrderEntity", description = "$!{table.comment}")
public class OrderEntity extends Model<OrderEntity> {

    @Schema(description = "订单ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "创建时间")
    @TableField("created")
    private String created;

    @Schema(description = "下单用户ID")
    @TableField("user")
    private Long user;

    @Schema(description = "下单课程ID")
    @TableField("course")
    private Long course;

    @Schema(description = "订单状态：0待支付 1已支付 2已取消")
    @TableField("status")
    private Integer status;

    @Schema(description = "订单价格")
    @TableField("price")
    private BigDecimal price;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
