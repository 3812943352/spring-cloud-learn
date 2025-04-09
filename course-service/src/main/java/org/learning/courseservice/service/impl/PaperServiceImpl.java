/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 16:01:42
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-09 18:53:03
 * @FilePath: course-service/src/main/java/org/learning/courseservice/service/impl/PaperServiceImpl.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.commonmodule.resp.Result;
import org.learning.courseservice.entity.*;
import org.learning.courseservice.feignClient.VideoServiceFeignClient;
import org.learning.courseservice.mapper.CourseMapper;
import org.learning.courseservice.mapper.ERecordMapper;
import org.learning.courseservice.mapper.PaperMapper;
import org.learning.courseservice.mapper.TopicMapper;
import org.learning.courseservice.service.PaperService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 王博
 * @since 2025-03-14
 */
@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, PaperEntity> implements PaperService {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final VideoServiceFeignClient videoServiceFeignClient;
    private final CourseMapper courseMapper;
    private final TopicMapper topicMapper;
    private final ERecordMapper eRecordMapper;

    public PaperServiceImpl(VideoServiceFeignClient videoServiceFeignClient, CourseMapper courseMapper, TopicMapper topicMapper, ERecordMapper eRecordMapper) {
        this.videoServiceFeignClient = videoServiceFeignClient;
        this.courseMapper = courseMapper;
        this.topicMapper = topicMapper;
        this.eRecordMapper = eRecordMapper;
    }

    @Override
    public Result<?> addPaper(PaperEntity paperEntity) {
        try {
            Integer courseID = paperEntity.getCourse();
            CourseEntity courseEntity = courseMapper.selectOne(new QueryWrapper<CourseEntity>().eq("id", courseID));
            if (courseEntity == null) {
                return Result.failure(202, "课程不存在");
            }
            if (courseEntity.getPaper() == 1) {
                return Result.failure(202, "该课程已添加试卷");
            }
            courseEntity.setPaper(1);
//            List<PaperEntity.Topics> topicsList = new ArrayList<>();
            long date = System.currentTimeMillis() / 1000;
            paperEntity.setCreated(date);
            paperEntity.setUpdated(date);
//            paperEntity.setTopics(topicsList);
            courseMapper.updateById(courseEntity);
            boolean save = save(paperEntity);
            if (save) {
                return Result.success(200, "添加成功");
            }
            return Result.failure(202, "添加失败");
        } catch (Exception e) {
            return Result.failure(202, "添加失败" + e.getMessage());
        }
    }

    @Override
    public Result<?> updatePaper(PaperEntity paperEntity) {
        // 获取当前试卷的详细信息
        PaperEntity paper = this.getById(paperEntity.getId());
        if (paper == null) {
            return Result.failure("试卷不存在");
        }

        // 获取旧课程的 ID 和课程实体
        Integer oldCourseId = paper.getCourse();
        CourseEntity oldCourse = courseMapper.selectOne(new QueryWrapper<CourseEntity>().eq("id", oldCourseId));
        if (oldCourse != null) {
            // 更新旧课程的 paper 字段为 0
            oldCourse.setPaper(0);
            int oldCourseUpdateResult = courseMapper.updateById(oldCourse);
            if (oldCourseUpdateResult <= 0) {
                return Result.failure("更新旧课程的试卷失败");
            }
        } else {
            // 如果旧课程不存在，记录日志或直接忽略（根据业务需求）
            System.out.println("旧课程不存在，无法更新试卷");
        }

        // 获取新课程的 ID 和课程实体
        Integer newCourseId = paperEntity.getCourse();
        CourseEntity newCourse = courseMapper.selectOne(new QueryWrapper<CourseEntity>().eq("id", newCourseId));
        if (newCourse == null) {
            return Result.failure("所选课程不存在");
        }

        // 更新新课程的 paper 字段为 1
        newCourse.setPaper(1);
        int newCourseUpdateResult = courseMapper.updateById(newCourse);
        if (newCourseUpdateResult <= 0) {
            return Result.failure("更新新课程的 paper 字段失败");
        }

        // 更新试卷实体
        boolean update = updateById(paperEntity);
        if (update) {
            return Result.success(200, "修改成功");
        }
        return Result.failure(202, "修改失败");
    }


    @Override
    public Result<?> delPaper(int id, int course) {
        CourseEntity courseEntity = courseMapper.selectOne(new QueryWrapper<CourseEntity>().eq("id", course));
        courseEntity.setPaper(0);
        courseMapper.updateById(courseEntity);
        boolean removeById = removeById(id);
        if (removeById) {
            return Result.success(200, "删除成功");
        }
        return Result.failure(202, "删除失败");
    }


    @Override
    public Result<?> get(int pageNum, int pageSize) {
        try {
            Page<PaperEntity> resultPage = new Page<>(pageNum, pageSize);
            QueryWrapper<PaperEntity> queryWrapper = new QueryWrapper<>();
            resultPage = this.page(resultPage, queryWrapper);
            return Result.success(resultPage);
        } catch (Exception e) {
            return Result.failure(202, "查询失败" + e.getMessage());
        }
    }

    @Override
    public Result<?> blur(int pageNum, int pageSize, String word) {
        Page<PaperEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<PaperEntity> queryWrapper = new QueryWrapper<>();
        if (word != null && !word.isEmpty()) {
            queryWrapper.or(wrapper -> wrapper
                    .like("name", word)

            );
        }
        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage, "请求成功");
    }

    @Override
    public Result<?> date(int pageNum, int pageSize, long startTime, long endTime) {
        Page<PaperEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<PaperEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("created", startTime, endTime);
        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage);
    }

    @Override
    public Result<?> listCourse() {
        List<PaperEntity> paperEntities = this.list(new QueryWrapper<PaperEntity>().select("ID", "name"));

        List<Map<String, Object>> formattedList = new ArrayList<>();
        for (PaperEntity paper : paperEntities) {
            Map<String, Object> map = new HashMap<>();
            map.put("value", paper.getId());
            map.put("label", paper.getName());
            formattedList.add(map);
        }

        return Result.success(formattedList);
    }

    @Override
    public Result<?> getTopic(int user, int course, String phone) {
        // 检查用户是否完成课程学习
        Result<?> isDoneResult = this.videoServiceFeignClient.isDone(user, course);
        Boolean isDone = (Boolean) isDoneResult.getData();
        if (!isDone) {
            return Result.failure("请先完成课程学习");
        }
        ERecordEntity eRecordEntity = eRecordMapper.selectOne(new QueryWrapper<ERecordEntity>()
                .eq("paper", course)
                .eq("user", phone)
                .eq("pass", 1)
        );
        if (eRecordEntity != null) {
            return Result.success("您已通过考试，请前往用户中心查看证书");
        }
        // 获取试卷信息
        PaperEntity paperEntity = this.getOne(new QueryWrapper<PaperEntity>()
                .eq("course", course)
        );
        if (paperEntity == null) {
            return Result.failure("未找到该课程的试卷信息");
        }

        // 获取题目列表
        PaperEntity.Topics topics = paperEntity.getTopics();
        System.out.println(topics);
        List<Integer> judgeList = topics.getJudge();
        List<Integer> radioList = topics.getRadio();
        List<Integer> choiceList = topics.getChoice();
        if (paperEntity.getSequence() == 1) {
            shuffleList(judgeList);
            shuffleList(radioList);
            shuffleList(choiceList);
        }


        // 存储处理后的题目数据
        Map<String, Object> resultTopics = new HashMap<>();
        resultTopics.put("paper", paperEntity);
        // 处理判断题
        if (judgeList != null && !judgeList.isEmpty()) {
            List<TopicEntity> listJudge = topicMapper.selectList(new QueryWrapper<TopicEntity>()
                    .in("id", judgeList));
            if (paperEntity.getOptions() == 1) {
                shuffleList(listJudge);
            }
            processOptions(listJudge);
            resultTopics.put("judge", listJudge);
        }

        // 处理单选题
        if (radioList != null && !radioList.isEmpty()) {
            List<TopicEntity> listRadio = topicMapper.selectList(new QueryWrapper<TopicEntity>()
                    .in("id", radioList));
            if (paperEntity.getOptions() == 1) {
                shuffleList(listRadio);
            }
            processOptions(listRadio);
            resultTopics.put("radio", listRadio);
        }

        // 处理多选题
        if (choiceList != null && !choiceList.isEmpty()) {
            List<TopicEntity> listChoice = topicMapper.selectList(new QueryWrapper<TopicEntity>()
                    .in("id", choiceList));
            if (paperEntity.getOptions() == 1) {
                shuffleList(listChoice);
            }
            processOptions(listChoice);
            resultTopics.put("choice", listChoice);
        }

        // 返回最终结果
        return Result.success(resultTopics);
    }


    private void shuffleList(List<?> list) {
        if (list != null && !list.isEmpty()) {
            Collections.shuffle(list);
        }
    }

    private void processOptions(List<TopicEntity> topicList) {
        for (TopicEntity topic : topicList) {
            // 清空题目描述
            topic.setParse(null);

            // 获取原始 options 数据
            List<?> rawOptions = topic.getOptions();

            // 如果 options 是 null 或空列表，直接跳过
            if (rawOptions == null || rawOptions.isEmpty()) {
                continue;
            }

            // 确保 options 是 List<TopicEntity.Option> 类型
            List<TopicEntity.Option> options = new ArrayList<>();
            for (Object optionObj : rawOptions) {
                if (optionObj instanceof TopicEntity.Option) {
                    // 如果已经是 Option 类型，直接添加
                    options.add((TopicEntity.Option) optionObj);
                } else if (optionObj instanceof Map) {
                    // 如果是 Map，转换为 Option 对象
                    Map<?, ?> map = (Map<?, ?>) optionObj;
                    String option = (String) map.get("option");
                    Boolean isAnswer = (Boolean) map.get("isAnswer");

                    // 创建新的 Option 对象并设置字段值
                    TopicEntity.Option newOption = new TopicEntity.Option();
                    newOption.setOption(option);
                    newOption.setIsAnswer(isAnswer);
                    options.add(newOption);
                }
            }

            // 更新 topic 的 options 字段
            topic.setOptions(options);

            // 替换 isAnswer 为 null
            for (TopicEntity.Option option : options) {
                option.setIsAnswer(null);
            }
        }
    }
}
