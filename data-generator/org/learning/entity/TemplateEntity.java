package org.learning.entity;

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
@TableName("template")
@Schema(name = "TemplateEntity", description = "$!{table.comment}")
public class TemplateEntity extends Model<TemplateEntity> {

    @Schema(description = "模板ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "证书名称")
    @TableField("name")
    private String name;

    @Schema(description = "证书内容")
    @TableField("content")
    private String content;

    @Schema(description = "颁发机构")
    @TableField("issure")
    private String issure;

    @Schema(description = "颁发人员")
    @TableField("people")
    private String people;

    @Schema(description = "颁发条件")
    @TableField("condition")
    private String condition;

    @Schema(description = "证书背景图片路径")
    @TableField("path")
    private String path;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
