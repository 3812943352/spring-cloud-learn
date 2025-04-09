/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-03 00:50:22
 * @FilePath: video-service/src/main/java/org/learning/videoservice/entity/VideoEntity.java
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
@TableName("video")
@Schema(name = "VideoEntity", description = "$!{table.comment}")
public class VideoEntity extends Model<VideoEntity> {

    @Schema(description = "视频ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "视频标题，最大长度255字符，不可为空")
    @TableField("title")
    private String title;

    @Schema(description = "视频描述")
    @TableField("des")
    private String des;

    @Schema(description = "视频内容存储路径，最大长度500字符，不可为空")
    @TableField("content_path")
    private String contentPath;

    @Schema(description = "上传用户ID，只能是admin用户，不可为空")
    @TableField("uploader")
    private Long uploader;

    @Schema(description = "创建时间，默认值为当前时间戳")
    @TableField("created")
    private Long created;

    @Schema(description = "更新时间")
    @TableField("updated")
    private Long updated;

    @Schema(description = "所属课程ID")
    @TableField("class_id")
    private Long classId;

    @Schema(description = "视频时长")
    @TableField("dur")
    private double dur;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
