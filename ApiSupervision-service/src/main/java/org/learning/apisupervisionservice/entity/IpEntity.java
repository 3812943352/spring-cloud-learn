/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 14:15:41
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-13 14:43:09
 * @FilePath: ApiSupervision-service/src/main/java/org/learning/apisupervisionservice/entity/IpEntity.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.apisupervisionservice.entity;

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
@TableName("ip")
@Schema(name = "IpEntity", description = "$!{table.comment}")
public class IpEntity extends Model<IpEntity> {

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField("ip")
    private String ip;

    @TableField("time")
    private Long time;

    @TableField("reason")
    private String reason;

    @TableField("bantime")
    private Long bantime;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
