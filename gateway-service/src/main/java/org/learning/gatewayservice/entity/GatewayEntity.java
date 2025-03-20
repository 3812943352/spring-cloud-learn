/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-12 16:56:13
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-12 19:57:23
 * @FilePath: gateway-service/src/main/java/org/learning/gatewayservice/entity/GatewayEntity.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.gatewayservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author 王博
 * @since 2025-03-12
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("gateway")
@Schema(name = "GatewayEntity", description = "$!{table.comment}")
public class GatewayEntity extends Model<GatewayEntity> {

    @Schema(description = "API请求ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "请求用户名")
    @TableField("username")
    private String username;

    @Schema(description = "请求地址")
    @TableField("req_add")
    private String reqAdd;

    @Schema(description = "请求方法")
    @TableField("req_method")
    private String reqMethod;

    @Schema(description = "请求ip")
    @TableField("req_ip")
    private String reqIp;

    @Schema(description = "请求用户字符串")
    @TableField("ua")
    private String ua;

    @Schema(description = "请求控制器")
    @TableField("req_controller")
    private String reqController;

    @Schema(description = "请求接口名")
    @TableField("req_name")
    private String reqName;

    @Schema(description = "请求状态码")
    @TableField("req_code")
    private String reqCode;

    @Schema(description = "异常信息")
    @TableField("errorinfo")
    private String errorinfo;

    @Schema(description = "请求体格式")
    @TableField("ContentType")
    private String contentType;

    @Schema(description = "请求体长度")
    @TableField("ContentLength")
    private Long contentLength;

    @Schema(description = "请求uri")
    @TableField("uri")
    private String uri;

    @Schema(description = "是否jwt验证，0否，1是")
    @TableField("jwt")
    private String jwt;

    @Schema(description = "请求时间")
    @TableField("req_time")
    private Long reqTime;

    @Schema(description = "响应时间")
    @TableField("res_time")
    private Long resTime;

    @Schema(description = "耗时")
    @TableField("ms")
    private Long ms;

    @Schema(description = "是否封禁，0否，1是")
    @TableField("isBan")
    private Integer isBan;

    @Schema(description = "封禁过期时间")
    @TableField("banTime")
    private Long banTime;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
