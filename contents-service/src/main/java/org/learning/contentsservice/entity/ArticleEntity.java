/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 09:01:55
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-14 09:07:03
 * @FilePath: contents-service/src/main/java/org/learning/contentsservice/entity/ArticleEntity.java
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
@TableName("article")
@Schema(name = "ArticleEntity", description = "$!{table.comment}")
public class ArticleEntity extends Model<ArticleEntity> {

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "文章类别ID")
    @TableField("type")
    private Integer type;

    @Schema(description = "浏览量")
    @TableField("visit")
    private Integer visit;

    @Schema(description = "收藏量")
    @TableField("collection")
    private Integer collection;

    @Schema(description = "点赞量")
    @TableField("like")
    private Integer like;

    @Schema(description = "评论量")
    @TableField("comment")
    private Integer comment;

    @Schema(description = "创建时间")
    @TableField("created")
    private Long created;

    @Schema(description = "更新时间")
    @TableField("updated")
    private Long updated;

    @Schema(description = "内容")
    @TableField("content")
    private String content;

    @Schema(description = "标题")
    @TableField("title")
    private String title;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
