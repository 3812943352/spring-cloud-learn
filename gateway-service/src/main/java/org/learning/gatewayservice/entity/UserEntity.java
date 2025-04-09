/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 09:50:21
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-08 00:54:24
 * @FilePath: gateway-service/src/main/java/org/learning/gatewayservice/entity/UserEntity.java
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
 * @since 2025-03-13
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("user")
@Schema(name = "UserEntity", description = "$!{table.comment}")
public class UserEntity extends Model<UserEntity> {

    @Schema(description = "用户id")
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "用户名")
    @TableField("username")
    private String username;

    @Schema(description = "密码")
    @TableField("pwd")
    private String pwd;

    @Schema(description = "用户角色，0超管，1管理，2普通用户")
    @TableField("role")
    private Integer role;

    @Schema(description = "权限，0超管，1管理，2普通用户")
    @TableField("auth")
    private String auth;

    @Schema(description = "注册时间")
    @TableField("created")
    private Long created;

    @Schema(description = "上次登录时间")
    @TableField("last_login")
    private Long lastLogin;

    @Schema(description = "手机号")
    @TableField("phone")
    private String phone;

    @Schema(description = "性别")
    @TableField("sex")
    private String sex;

    @Schema(description = "头像")
    @TableField("img")
    private String img;

    @Schema(description = "身份证号")
    @TableField("idNum")
    private String idNum;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
