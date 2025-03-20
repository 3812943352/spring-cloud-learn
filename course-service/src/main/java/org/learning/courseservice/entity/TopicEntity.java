/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 16:01:26
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-19 20:42:38
 * @FilePath: course-service/src/main/java/org/learning/courseservice/entity/TopicEntity.java
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
@TableName(value = "topic", autoResultMap = true)
@Schema(name = "TopicEntity", description = "$!{table.comment}")
public class TopicEntity extends Model<TopicEntity> {

    @Schema(description = "题目ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "题目类型 0 单选  1多选  2判断")
    @TableField("type")
    private Integer type;

    @Schema(description = "题干")
    @TableField("stem")
    private String stem;

    @Schema(description = "题目分数")
    @TableField("scores")
    private String scores;

    @Schema(description = "答案解析")
    @TableField("parse")
    private String parse;

    @Schema(description = "选项列表")
    @TableField(value = "options", typeHandler = JacksonTypeHandler.class)
    private List<Option> options;

    @Override
    public Serializable pkVal() {
        return this.id;
    }

    public static class Option {
        private String option;
        private Boolean isAnswer;

        // Getters and Setters
        public String getOption() {
            return option;
        }

        public void setOption(String option) {
            this.option = option;
        }

        public Boolean getIsAnswer() {
            return isAnswer;
        }

        public void setIsAnswer(Boolean isAnswer) {
            this.isAnswer = isAnswer;
        }
    }
}
