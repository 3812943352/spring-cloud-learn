/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-08 04:39:41
 * @FilePath: course-service/src/main/java/org/learning/courseservice/entity/CertEntity.java
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
@TableName("cert")
@Schema(name = "CertEntity", description = "$!{table.comment}")
public class CertEntity extends Model<CertEntity> {

    @Schema(description = "证书ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "课程名称")
    @TableField("course")
    private Integer course;

    @Schema(description = "证书编号")
    @TableField("cert")
    private String cert;

    @Schema(description = "发放时间")
    @TableField("created")
    private Long created;

    @Schema(description = "证书模板ID")
    @TableField("tem")
    private Integer tem;

    @Schema(description = "用户ID")
    @TableField("user")
    private Integer user;

    @Schema(description = "用户手机号")
    @TableField("phone")
    private String phone;

    @Schema(description = "用户身份证号")
    @TableField("idNum")
    private String idNum;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
