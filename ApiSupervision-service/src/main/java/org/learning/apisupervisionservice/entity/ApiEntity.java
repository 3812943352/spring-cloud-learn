/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-12 16:53:42
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-12 16:54:57
 * @FilePath: ApiSupervision-service/src/main/java/org/learning/apisupervisionservice/entity/ApiEntity.java
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
 * @since 2025-03-12
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("api")
@Schema(name = "ApiEntity", description = "$!{table.comment}")
public class ApiEntity extends Model<ApiEntity> {

    @Schema(description = "api接口ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "api标题")
    @TableField("api_title")
    private String apiTitle;

    @Schema(description = "api描述")
    @TableField("api_des")
    private String apiDes;

    @Schema(description = "开放方式")
    @TableField("open_method")
    private String openMethod;

    @Schema(description = "api源单位")
    @TableField("source")
    private String source;

    @Schema(description = "接口地址")
    @TableField("api")
    private String api;

    @Schema(description = "api控制器")
    @TableField("api_controller")
    private String apiController;

    @Schema(description = "api方法")
    @TableField("api_method")
    private String apiMethod;

    @Schema(description = "api数据格式")
    @TableField("api_format")
    private String apiFormat;

    @Schema(description = "请求示例")
    @TableField("api_demo")
    private String apiDemo;

    @Schema(description = "api表名")
    @TableField("api_table")
    private String apiTable;

    @Schema(description = "调用次数")
    @TableField("times")
    private Integer times;

    @Schema(description = "访问次数")
    @TableField("visits")
    private Integer visits;

    @Schema(description = "错误次数")
    @TableField("errors")
    private Integer errors;

    @Schema(description = "权限等级")
    @TableField("auth")
    private String auth;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
