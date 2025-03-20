/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 16:01:19
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-20 20:13:47
 * @FilePath: course-service/src/main/java/org/learning/courseservice/controller/TopicController.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.controller;

import com.common.commonmodule.resp.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.learning.courseservice.entity.TopicEntity;
import org.learning.courseservice.service.TopicService;
import org.learning.courseservice.service.impl.TopicServiceImpl;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 王博
 * @since 2025-03-14
 */
@RestController
@RequestMapping("/course")
public class TopicController {
    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @Operation(summary = "创建题目")
    @RequestMapping("/addTopic")
    public Result<?> addTopic(@RequestBody TopicEntity topicEntity) {
        return topicService.addTopic(topicEntity);
    }

    @Operation(summary = "更新题目")
    @RequestMapping("/updateTopic")
    public Result<?> updateTopic(@RequestBody TopicEntity topicEntity) {
        return topicService.updateTopic(topicEntity);
    }

    @Operation(summary = "删除题目")
    @RequestMapping("/delTopic")
    public Result<?> deleteTopic(@RequestParam("id") Integer id) {
        return topicService.deleteTopic(id);
    }

    @Operation(summary = "题目分页")
    @PostMapping(value = "/getTopic")
    public Result<?> getPaper(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                              @RequestParam(value = "type", defaultValue = "0") int type
    ) {
        return topicService.get(pageNum, pageSize, type);
    }

    @Operation(summary = "模糊查询")
    @PostMapping(value = "/blurTopic")
    public Result<?> blur(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                          @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                          @RequestParam(value = "word", defaultValue = "") String word,
                          @RequestParam(value = "type", defaultValue = "0") String type
    ) {
        return topicService.blur(pageNum, pageSize, word, type);
    }
}
