/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 14:15:33
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-13 15:43:36
 * @FilePath: ApiSupervision-service/src/main/java/org/learning/apisupervisionservice/controller/GatewayController.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.apisupervisionservice.controller;

import com.common.commonmodule.resp.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.learning.apisupervisionservice.service.GatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 王博
 * @since 2025-03-13
 */
@RestController
@RequestMapping("/ApiSuperVision")
@Tag(name = "GatewayController", description = "网关管理")
public class GatewayController {

    private GatewayService gs;


    @Autowired
    public void setGs(GatewayService gs) {
        this.gs = gs;
    }


    @Operation(summary = "请求信息分页")
    @PostMapping(value = "/getPage")
    public Result<?> getAll(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        return this.gs.getAll(pageNum, pageSize);
    }

    @Operation(summary = "模糊搜索")
    @PostMapping(value = "/getBlur")
    public Result<?> getBlur(@RequestParam("pageNum") int pageNum,
                             @RequestParam("pageSize") int pageSize,
                             @RequestParam("word") String word
    ) {
        return this.gs.getBlur(pageNum, pageSize, word);
    }

    @Operation(summary = "日期搜索")
    @PostMapping(value = "/getDate")
    public Result<?> getDate(@RequestParam("pageNum") int pageNum,
                             @RequestParam("pageSize") int pageSize,
                             @RequestParam("start") long start,
                             @RequestParam("end") long end
    ) {
        return this.gs.getDate(pageNum, pageSize, start, end);
    }

    @Operation(summary = "删除记录")
    @PostMapping(value = "/delRecord")
    public Result<?> getDate(@RequestParam("id") int id) {
        return this.gs.del(id);
    }
}
