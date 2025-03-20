/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-12 16:53:42
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-13 16:25:41
 * @FilePath: ApiSupervision-service/src/main/java/org/learning/apisupervisionservice/controller/ApiController.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.apisupervisionservice.controller;

import com.common.commonmodule.resp.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.learning.apisupervisionservice.entity.ApiEntity;
import org.learning.apisupervisionservice.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 王博
 * @since 2025-03-12
 */
@RestController
@RequestMapping("/ApiSuperVision")
@Tag(name = "ApiController", description = "API管理")
public class ApiController {

    private ApiService apiService;


    @Autowired
    public void setGs(ApiService apiService) {
        this.apiService = apiService;
    }

    @Operation(summary = "首页数据")
    @PostMapping(value = "/homeData")
    public Result<?> homeData() {
        return apiService.homeData();
    }

    @Operation(summary = "折线图")
    @PostMapping(value = "/getLines")
    public Result<?> getLine() {
        return apiService.getLine();
    }


    @Operation(summary = "图表")
    @PostMapping(value = "/getCharts")
    public Result<?> getCharts(@RequestParam("start") long start, @RequestParam("end") long end) {
        return apiService.getCharts(start, end);
    }

    @Operation(summary = "添加一个新的API")
    @PostMapping(value = "/addApi")
    public Result<?> addApi(@RequestBody ApiEntity apiEntity) {
        return apiService.addApi(apiEntity);
    }

    @Operation(summary = "删除一个API")
    @PostMapping(value = "/deleteApi")
    public Result<?> deleteApi(@RequestBody ApiEntity apiEntity) {
        return apiService.deleteApi(apiEntity);
    }

    @Operation(summary = "更新一个API")
    @PostMapping(value = "/updateApi")
    public Result<?> updateApi(@RequestBody ApiEntity apiEntity) {
        return apiService.updateApi(apiEntity);
    }

    @Operation(summary = "获取所有API（分页）")
    @PostMapping(value = "/getAllApi")
    public Result<?> getAllApi(
            @RequestParam("pageNum") int pageNum,
            @RequestParam("pageSize") int pageSize) {
        return apiService.getAllApi(pageNum, pageSize);
    }


    @Operation(summary = "模糊搜索")
    @PostMapping(value = "/getApiBlur")
    public Result<?> getBlur(@RequestParam("pageNum") int pageNum,
                             @RequestParam("pageSize") int pageSize,
                             @RequestParam("word") String word
    ) {
        return apiService.getBlur(pageNum, pageSize, word);
    }
}
