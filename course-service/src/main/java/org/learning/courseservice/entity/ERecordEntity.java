/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 16:01:26
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-14 16:04:12
 * @FilePath: course-service/src/main/java/org/learning/courseservice/entity/ERecordEntity.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.entity;

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
@TableName("e_record")
@Schema(name = "ERecordEntity", description = "$!{table.comment}")
public class ERecordEntity extends Model<ERecordEntity> {

    @Schema(description = "考试ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "考试人")
    @TableField("user")
    private Integer user;

    @Schema(description = "试卷ID")
    @TableField("paper")
    private Integer paper;

    @Schema(description = "开始时间")
    @TableField("start")
    private Long start;

    @Schema(description = "得分")
    @TableField("scores")
    private Integer scores;

    @Schema(description = "用户选项列表")
    @TableField("options")
    private String options;

    @Schema(description = "用户选择对错列表")
    @TableField("right")
    private String right;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
