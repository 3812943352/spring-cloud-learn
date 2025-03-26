/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 09:01:55
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-25 22:02:47
 * @FilePath: contents-service/src/main/java/org/learning/contentsservice/entity/ArticleEntity.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.contentsservice.entity;

import com.baomidou.mybatisplus.annotation.*;
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
    @TableField(value = "type", updateStrategy = FieldStrategy.IGNORED)
    private Integer type;

    @Schema(description = "浏览量")
    @TableField(value = "visit", updateStrategy = FieldStrategy.IGNORED)
    private Integer visit;

    @Schema(description = "收藏量")
    @TableField(value = "collection", updateStrategy = FieldStrategy.IGNORED)
    private Integer collection;

    @Schema(description = "点赞量")
    @TableField(value = "likes", updateStrategy = FieldStrategy.IGNORED)
    private Integer likes;

    @Schema(description = "评论量")
    @TableField(value = "comment", updateStrategy = FieldStrategy.IGNORED)
    private Integer comment;

    @Schema(description = "创建时间")
    @TableField(value = "created", updateStrategy = FieldStrategy.IGNORED)
    private Long created;

    @Schema(description = "更新时间")
    @TableField(value = "updated", updateStrategy = FieldStrategy.IGNORED)
    private Long updated;

    @Schema(description = "内容")
    @TableField(value = "content", updateStrategy = FieldStrategy.IGNORED)
    private String content;

    @Schema(description = "标题")
    @TableField(value = "title", updateStrategy = FieldStrategy.IGNORED)
    private String title;

    @Schema(description = "图片1")
    @TableField(value = "img1", updateStrategy = FieldStrategy.IGNORED)
    private String img1;

    @Schema(description = "图片2")
    @TableField(value = "img2", updateStrategy = FieldStrategy.IGNORED)
    private String img2;

    @Schema(description = "图片3")
    @TableField(value = "img3", updateStrategy = FieldStrategy.IGNORED)
    private String img3;

    @Schema(description = "图片4")
    @TableField(value = "img4", updateStrategy = FieldStrategy.IGNORED)
    private String img4;

    @Schema(description = "图片5")
    @TableField(value = "img5", updateStrategy = FieldStrategy.IGNORED)
    private String img5;


    @Schema(description = "是否首页显示")
    @TableField(value = "isShow", updateStrategy = FieldStrategy.IGNORED)
    private Integer isShow;

    @Schema(description = "是否推荐")
    @TableField(value = "suggest", updateStrategy = FieldStrategy.IGNORED)
    private Integer suggest;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
