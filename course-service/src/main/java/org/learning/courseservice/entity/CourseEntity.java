/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-24 11:35:22
 * @FilePath: course-service/src/main/java/org/learning/courseservice/entity/CourseEntity.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

import java.io.Serializable;
import java.math.BigDecimal;

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
@TableName(value = "course", autoResultMap = true)
@Schema(name = "ClassEntity", description = "$!{table.comment}")
public class CourseEntity extends Model<CourseEntity> {

    @Schema(description = "课程ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "课程价格")
    @TableField("price")
    private BigDecimal price;

    @Schema(description = "课程描述")
    @TableField("des")
    private String des;

    @Schema(description = "课程名称")
    @TableField("name")
    private String name;

    @Schema(description = "创建时间")
    @TableField("created")
    private Long created;

    @Schema(description = "更新时间")
    @TableField("updated")
    private Long updated;

    @Schema(description = "封面存储路径")
    @TableField("cover")
    private String cover;

    @Schema(description = "课程类别")
    @TableField("type")
    private Integer type;

    @Schema(description = "课程点赞数")
    @TableField("likeNum")
    private Integer likeNum;

    @Schema(description = "课程收藏数")
    @TableField("col")
    private Integer col;

    @Schema(description = "课程评论数")
    @TableField("commentNum")
    private Integer commentNum;

    @Schema(description = "课程是否添加试卷")
    @TableField("paper")
    private Integer paper;

    @Schema(description = "课程是否添加视频")
    @TableField(value = "video", typeHandler = JacksonTypeHandler.class)
    private List<Integer> video;


    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
