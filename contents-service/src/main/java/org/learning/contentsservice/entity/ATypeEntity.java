/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 09:01:36
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-14 09:07:04
 * @FilePath: contents-service/src/main/java/org/learning/contentsservice/entity/ATypeEntity.java
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
@TableName("a_type")
@Schema(name = "ATypeEntity", description = "$!{table.comment}")
public class ATypeEntity extends Model<ATypeEntity> {

    @Schema(description = "文章类别表")
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "文章类别")
    @TableField("type")
    private String type;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
