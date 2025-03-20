/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 16:01:26
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-20 23:02:18
 * @FilePath: course-service/src/main/java/org/learning/courseservice/entity/PaperEntity.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
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
@TableName(value = "paper", autoResultMap = true)
@Schema(name = "PaperEntity", description = "$!{table.comment}")
public class PaperEntity extends Model<PaperEntity> {

    @Schema(description = "试卷ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "试卷所属课程")
    @TableField("course")
    private Integer course;

    @Schema(description = "试卷名称")
    @TableField("name")
    private String name;

    @Schema(description = "试卷下试题ID列表")
    @TableField(value = "topics", typeHandler = JacksonTypeHandler.class)
    private List<Integer> topics;

    @Schema(description = "及格分数")
    @TableField("pass")
    private Integer pass;

    @Schema(description = "答题时间，单位秒")
    @TableField("time")
    private Long time;

    @Schema(description = "题序打乱 0否1是")
    @TableField("sequence")
    private Integer sequence;

    @Schema(description = "选项打乱 0否1是")
    @TableField("options")
    private Integer options;

    @Schema(description = "难度1-5，对应1-5星")
    @TableField("difficulty")
    private Integer difficulty;

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

    public static class Topics {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }


    }
}
