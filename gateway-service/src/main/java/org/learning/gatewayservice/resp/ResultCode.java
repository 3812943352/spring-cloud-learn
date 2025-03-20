/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 15:10:35
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-13 15:24:46
 * @FilePath: gateway-service/src/main/java/org/learning/gatewayservice/resp/ResultCode.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.gatewayservice.resp;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    SUCCESS(200, "成功"),
    REQUEST_ERROR(201, "参数校验失败"),
    FAILURE(202, "失败");

    private final int code;
    private final String msg;
}