/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-13 22:43:25
 * @FilePath: video-service/src/main/java/org/learning/videoservice/entity/ProgressEntity.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.videoservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
@TableName("progress")
@Schema(name = "ProgressEntity", description = "$!{table.comment}")
public class ProgressEntity extends Model<ProgressEntity> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user")
    private Long user;

    @TableField("video")
    private Long video;

    @TableField("progress")
    private Integer progress;

    @TableField("last")
    private Long last;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
