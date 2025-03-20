/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 09:55:29
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-13 09:56:26
 * @FilePath: user-service/src/main/java/org/learning/userservice/entity/AuthEntity.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.userservice.entity;

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
@TableName("auth")
@Schema(name = "AuthEntity", description = "$!{table.comment}")
public class AuthEntity extends Model<AuthEntity> {

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "页面url")
    @TableField("path")
    private String path;

    @Schema(description = "是否登录")
    @TableField("login")
    private String login;

    @Schema(description = "权限")
    @TableField("auth")
    private String auth;

    @Schema(description = "描述")
    @TableField("des")
    private String des;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
