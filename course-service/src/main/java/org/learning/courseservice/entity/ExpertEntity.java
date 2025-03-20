/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-14 15:19:51
 * @FilePath: course-service/src/main/java/org/learning/courseservice/entity/ExpertEntity.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.entity;

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
@TableName("expert")
@Schema(name = "ExpertEntity", description = "$!{table.comment}")
public class ExpertEntity extends Model<ExpertEntity> {

    @Schema(description = "专家ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "专家名字")
    @TableField("name")
    private String name;

    @Schema(description = "专家履历")
    @TableField("cv")
    private String cv;

    @Schema(description = "专家描述")
    @TableField("des")
    private String des;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
