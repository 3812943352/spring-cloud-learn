/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-19 22:36:07
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-20 08:32:08
 * @FilePath: common-module/src/main/java/com/common/commonmodule/req/IdParam.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package com.common.commonmodule.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdParam {
    @Schema(description = "ID，记录唯一标识")
    private Integer id;
}