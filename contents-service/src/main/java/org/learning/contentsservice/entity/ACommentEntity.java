/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 09:01:17
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-26 11:02:00
 * @FilePath: contents-service/src/main/java/org/learning/contentsservice/entity/ACommentEntity.java
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
@TableName("a_comment")
@Schema(name = "ACommentEntity", description = "$!{table.comment}")
public class ACommentEntity extends Model<ACommentEntity> {

    @Schema(description = "评论ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "评论内容")
    @TableField("content")
    private String content;

    @Schema(description = "评论人")
    @TableField("user")
    private String user;

    @Schema(description = "评论文章")
    @TableField("article")
    private String article;

    @Schema(description = "评论时间")
    @TableField("created")
    private Long created;

    @Schema(description = "是否显示")
    @TableField("isShow")
    private Integer isShow;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
