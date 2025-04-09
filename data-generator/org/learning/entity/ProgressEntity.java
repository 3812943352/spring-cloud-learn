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
 * @since 2025-04-03
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("progress")
@Schema(name = "ProgressEntity", description = "$!{table.comment}")
public class ProgressEntity extends Model<ProgressEntity> {

    @Schema(description = "进度ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "用户id")
    @TableField("user")
    private Integer user;

    @Schema(description = "视频ID")
    @TableField("video")
    private Integer video;

    @Schema(description = "观看进度百分比")
    @TableField("progress")
    private Integer progress;

    @Schema(description = "是否看完")
    @TableField("isDone")
    private Integer isDone;

    @Schema(description = "创建时间")
    @TableField("created")
    private Long created;

    @Schema(description = "更新时间")
    @TableField("updated")
    private Long updated;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
