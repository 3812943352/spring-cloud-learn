/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 15:58:06
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-30 13:00:26
 * @FilePath: contents-service/src/main/java/org/learning/contentsservice/entity/SettingEntity.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.contentsservice.entity;

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
 * @since 2025-03-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("setting")
@Schema(name = "SettingEntity", description = "$!{table.comment}")
public class SettingEntity extends Model<SettingEntity> {
    @Schema(description = "配置ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "首页轮播图1路径")
    @TableField("img1")
    private String img1;

    @Schema(description = "首页轮播图2路径")
    @TableField("img2")
    private String img2;

    @Schema(description = "首页轮播图3路径")
    @TableField("img3")
    private String img3;

    @Schema(description = "首页轮播图4路径")
    @TableField("img4")
    private String img4;

    @Schema(description = "首页轮播图5路径")
    @TableField("img5")
    private String img5;

    @Schema(description = "举报电话")
    @TableField("phone")
    private String phone;

    @Schema(description = "举报邮箱")
    @TableField("email")
    private String email;

    @Schema(description = "备案号")
    @TableField("num")
    private String num;

    @Schema(description = "版权")
    @TableField("cr")
    private String cr;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
