/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-14 16:01:42
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-08 04:40:10
 * @FilePath: course-service/src/main/java/org/learning/courseservice/service/impl/TopicServiceImpl.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.commonmodule.resp.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.learning.courseservice.entity.*;
import org.learning.courseservice.mapper.*;
import org.learning.courseservice.service.TopicService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 王博
 * @since 2025-03-14
 */
@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, TopicEntity> implements TopicService {

    private final PaperMapper paperMapper;
    private final ERecordMapper eRecordMapper;
    private final CourseMapper courseMapper;
    private final CertMapper certMapper;
    private final UserMapper userMapper;

    public TopicServiceImpl(PaperMapper paperMapper, ERecordMapper eRecordMapper, CourseMapper courseMapper, CertMapper certMapper, UserMapper userMapper) {
        this.paperMapper = paperMapper;
        this.eRecordMapper = eRecordMapper;
        this.courseMapper = courseMapper;
        this.certMapper = certMapper;
        this.userMapper = userMapper;
    }

    @Override
    public Result<?> addTopic(TopicEntity topicEntity) {
        if (this.save(topicEntity)) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }

    @Override
    public Result<?> updateTopic(TopicEntity topicEntity) {
        try {
            this.updateById(topicEntity);
            return Result.success();
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }

    }

    @Override
    public Result<?> deleteTopic(Integer id) {
        if (this.removeById(id)) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }

    @Override
    public Result<?> get(int pageNum, int pageSize, int type) {
        try {
            Page<TopicEntity> resultPage = new Page<>(pageNum, pageSize);
            QueryWrapper<TopicEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("type", type);
            resultPage = this.page(resultPage, queryWrapper);
            return Result.success(resultPage);
        } catch (Exception e) {
            return Result.failure(202, "查询失败" + e.getMessage());
        }
    }

    @Override
    public Result<?> blur(int pageNum, int pageSize, String word, String type) {
        Page<TopicEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<TopicEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type);
        if (word != null && !word.trim().isEmpty()) {
            queryWrapper.and(wrapper -> wrapper.like("stem", word));
        }
        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage, "请求成功");
    }


    @Override
    public Result<?> scoring(List<Radio> radioAnswer, List<Choice> choiceAnswer, List<Judge> judgeAnswer, int course, String phone, int user) {
        try {
            ERecordEntity eRecordEntity = eRecordMapper.selectOne(new QueryWrapper<ERecordEntity>()
                    .eq("paper", course)
                    .eq("user", phone)
                    .eq("pass", 1)
            );
            if (eRecordEntity != null) {
                return Result.success("您已通过考试，请前往用户中心查看证书");
            }
            List<Map<String, Object>> radioResults = new ArrayList<>();
            List<Map<String, Object>> choiceResults = new ArrayList<>();
            List<Map<String, Object>> judgeResults = new ArrayList<>();

            int totalScore = 0;

            // 获取试卷信息
            PaperEntity paperEntity = paperMapper.selectOne(new QueryWrapper<PaperEntity>().eq("course", course));
            int passScore = paperEntity.getPass();

            // 处理单选题
            if (radioAnswer != null && !radioAnswer.isEmpty()) {
                for (Radio radio : radioAnswer) {
                    int id = radio.getId();
                    String userAnswer = radio.getCheck();
                    TopicEntity topicEntity = this.getById(id);

                    boolean isCorrect = processRadioAnswer(topicEntity, userAnswer);
                    Map<String, Object> result = new HashMap<>();
                    result.put("topic", topicEntity);
                    result.put("isCorrect", isCorrect);
                    radioResults.add(result);

                    if (isCorrect) {
                        totalScore += Integer.parseInt(topicEntity.getScores());
                    }
                }
            }

            // 处理多选题
            if (choiceAnswer != null && !choiceAnswer.isEmpty()) {
                for (Choice choice : choiceAnswer) {
                    int id = choice.getId();
                    List<String> userAnswers = choice.getCheck();
                    TopicEntity topicEntity = this.getById(id);

                    boolean isCorrect = processChoiceAnswer(topicEntity, userAnswers);
                    Map<String, Object> result = new HashMap<>();
                    result.put("topic", topicEntity);
                    result.put("isCorrect", isCorrect);
                    choiceResults.add(result);

                    if (isCorrect) {
                        totalScore += Integer.parseInt(topicEntity.getScores());
                    }
                }
            }

            // 处理判断题
            if (judgeAnswer != null && !judgeAnswer.isEmpty()) {
                for (Judge judge : judgeAnswer) {
                    int id = judge.getId();
                    String userAnswer = judge.getCheck();
                    TopicEntity topicEntity = this.getById(id);

                    boolean isCorrect = processJudgeAnswer(topicEntity, userAnswer);
                    Map<String, Object> result = new HashMap<>();
                    result.put("topic", topicEntity);
                    result.put("isCorrect", isCorrect);
                    judgeResults.add(result);

                    if (isCorrect) {
                        totalScore += Integer.parseInt(topicEntity.getScores());
                    }
                }
            }

            // 返回结果
            Map<String, Object> finalResult = new HashMap<>();
            finalResult.put("radio", radioResults);
            finalResult.put("choice", choiceResults);
            finalResult.put("judge", judgeResults);
            finalResult.put("totalScore", totalScore);
            ERecordEntity recordEntity = new ERecordEntity();

            if (totalScore > passScore) {
                recordEntity.setPass(1);
                CourseEntity courseEntity = courseMapper.selectOne(new QueryWrapper<CourseEntity>().eq("id", course));
                if (courseEntity == null) {
                    return Result.failure("课程不存在");
                }
                int tem = courseEntity.getTem();
                UserEntity userEntity = userMapper.selectOne(new QueryWrapper<UserEntity>().eq("id", user));
                CertEntity certEntity = new CertEntity();
                certEntity.setUser(user);
                certEntity.setTem(tem);
                certEntity.setCourse(course);
                certEntity.setCreated(System.currentTimeMillis() / 1000);
                certEntity.setCert(UUID.randomUUID().toString().substring(0, 18));
                certEntity.setPhone(userEntity.getPhone());
                certEntity.setIdNum(userEntity.getIdNum());
                certMapper.insert(certEntity);
                finalResult.put("result", "考试通过");
            } else {
                recordEntity.setPass(0);
                finalResult.put("result", "未通过考试");
            }

            // 记录考试结果
            recordEntity.setPaper(course);
            recordEntity.setScores(totalScore);
            recordEntity.setCreated(System.currentTimeMillis() / 1000);
            recordEntity.setUser(phone);
            eRecordMapper.insert(recordEntity);

            return Result.success(finalResult, "阅卷成功");
        } catch (Exception e) {
            return Result.failure("阅卷失败，请重新提交" + e.getMessage());
        }

    }

    private boolean processRadioAnswer(TopicEntity topic, String userAnswer) {
        List<?> rawOptions = topic.getOptions();
        List<TopicEntity.Option> options;

        if (rawOptions != null && !rawOptions.isEmpty()) {
            if (rawOptions.get(0) instanceof LinkedHashMap) {
                // 如果是 LinkedHashMap，进行反序列化
                ObjectMapper objectMapper = new ObjectMapper();
                options = rawOptions.stream()
                        .map(option -> objectMapper.convertValue(option, TopicEntity.Option.class))
                        .collect(Collectors.toList());
            } else {
                // 否则直接强制转换
                options = (List<TopicEntity.Option>) rawOptions;
            }
        } else {
            options = Collections.emptyList();
        }

        for (TopicEntity.Option option : options) {
            if (option.getIsAnswer() && option.getOption().equals(userAnswer)) {
                return true;
            }
        }
        return false;
    }

    private boolean processChoiceAnswer(TopicEntity topic, List<String> userAnswers) {
        List<?> rawOptions = topic.getOptions();
        List<TopicEntity.Option> options;

        // 检查并处理 rawOptions 类型
        if (rawOptions != null && !rawOptions.isEmpty()) {
            if (rawOptions.get(0) instanceof LinkedHashMap) {
                // 如果是 LinkedHashMap，进行反序列化
                ObjectMapper objectMapper = new ObjectMapper();
                options = rawOptions.stream()
                        .map(option -> objectMapper.convertValue(option, TopicEntity.Option.class))
                        .collect(Collectors.toList());
            } else {
                // 否则直接强制转换
                options = (List<TopicEntity.Option>) rawOptions;
            }
        } else {
            options = Collections.emptyList();
        }

        // 提取标准答案
        List<String> correctAnswers = options.stream()
                .filter(TopicEntity.Option::getIsAnswer)
                .map(TopicEntity.Option::getOption)
                .collect(Collectors.toList());

        // 比较用户答案和标准答案
        return correctAnswers.size() == userAnswers.size() &&
                correctAnswers.containsAll(userAnswers) &&
                userAnswers.containsAll(correctAnswers);
    }

    private boolean processJudgeAnswer(TopicEntity topic, String userAnswer) {
        List<?> rawOptions = topic.getOptions();
        List<TopicEntity.Option> options;

        // 检查并处理 rawOptions 类型
        if (rawOptions != null && !rawOptions.isEmpty()) {
            if (rawOptions.get(0) instanceof LinkedHashMap) {
                // 如果是 LinkedHashMap，进行反序列化
                ObjectMapper objectMapper = new ObjectMapper();
                options = rawOptions.stream()
                        .map(option -> objectMapper.convertValue(option, TopicEntity.Option.class))
                        .collect(Collectors.toList());
            } else {
                // 否则直接强制转换
                options = (List<TopicEntity.Option>) rawOptions;
            }
        } else {
            options = Collections.emptyList();
        }

        // 遍历选项，判断是否匹配
        for (TopicEntity.Option option : options) {
            if (option.getIsAnswer() && option.getOption().equals(userAnswer)) {
                return true;
            }
        }
        return false;
    }
}
