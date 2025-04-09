/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 22:32:16
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-08 17:28:56
 * @FilePath: course-service/src/main/java/org/learning/courseservice/service/impl/CertServiceImpl.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.commonmodule.resp.Result;
import org.learning.courseservice.entity.CertEntity;
import org.learning.courseservice.entity.CourseEntity;
import org.learning.courseservice.entity.TemplateEntity;
import org.learning.courseservice.mapper.CTypeMapper;
import org.learning.courseservice.mapper.CertMapper;
import org.learning.courseservice.mapper.CourseMapper;
import org.learning.courseservice.mapper.TemplateMapper;
import org.learning.courseservice.service.CertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 王博
 * @since 2025-03-13
 */
@Service
public class CertServiceImpl extends ServiceImpl<CertMapper, CertEntity> implements CertService {
    private final TemplateMapper templateMapper;
    private final CourseMapper courseMapper;
    private final CTypeMapper cTypeMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public CertServiceImpl(TemplateMapper templateMapper, CourseMapper courseMapper, CTypeMapper cTypeMapper) {
        this.templateMapper = templateMapper;
        this.courseMapper = courseMapper;
        this.cTypeMapper = cTypeMapper;
    }

    @Override
    public Result<?> get(int pageNum, int pageSize) {
        try {
            Page<CertEntity> resultPage = new Page<>(pageNum, pageSize);
            QueryWrapper<CertEntity> queryWrapper = new QueryWrapper<>();
            resultPage = this.page(resultPage, queryWrapper);
            return Result.success(resultPage);
        } catch (Exception e) {
            return Result.failure(202, "查询失败" + e);
        }
    }

    @Override
    public Result<?> blur(int pageNum, int pageSize, String word) {
        Page<CertEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<CertEntity> queryWrapper = new QueryWrapper<>();
        if (word != null && !word.isEmpty()) {
            queryWrapper.or(wrapper -> wrapper
                    .like("cert", word)

            );
        }
        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage, "请求成功");
    }

    @Override
    public Result<?> date(int pageNum, int pageSize, long startTime, long endTime) {
        Page<CertEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<CertEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("created", startTime, endTime);
        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage);
    }

    @Override
    public Result<?> userCert(String phone) {
        // 1. 查询用户的证书信息（只选择需要的字段）
        List<CertEntity> certEntityList = this.list(
                new QueryWrapper<CertEntity>()
                        .eq("phone", phone)
                        .select("id", "cert", "created", "tem", "course")
        );

        if (certEntityList == null || certEntityList.isEmpty()) {
            return Result.failure("未找到该用户的证书信息");
        }

        // 2. 构建返回结果：证书和对应的模板信息
        List<Map<String, Object>> resultList = new ArrayList<>();

        for (CertEntity certEntity : certEntityList) {
            // 查询证书模板信息
            TemplateEntity templateEntity = templateMapper.selectById(certEntity.getTem());
            if (templateEntity == null) {
                return Result.failure("未找到证书模板信息，证书ID：" + certEntity.getId());
            }
            int course = certEntity.getCourse();
            System.out.println(course);
            CourseEntity courseEntity = courseMapper.selectOne(new QueryWrapper<CourseEntity>().eq("id", course));


            // 构造返回数据
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("cert", certEntity.getCert());
            resultMap.put("created", certEntity.getCreated());
            resultMap.put("path", templateEntity.getPath());
            resultMap.put("isSure", templateEntity.getIssure());
            resultMap.put("name", templateEntity.getName());
            resultMap.put("content", templateEntity.getContent());
            resultMap.put("course", courseEntity.getName());
            resultList.add(resultMap);
        }

        // 3. 返回结果
        return Result.success(resultList);
    }


    @Override
    public Result<?> certValid(String idNum, String phone, String cert) {
        // 查询证书信息
        CertEntity certEntity = this.getOne(new QueryWrapper<CertEntity>().eq("cert", cert));
        if (certEntity == null) {
            return Result.failure("证书不存在");
        }

        // 获取完整手机号和身份证号
        String userPhone = certEntity.getPhone();
        String userIdNum = certEntity.getIdNum();

        // 验证手机号后四位
        if (userPhone == null || userPhone.length() < 4) {
            return Result.failure("用户手机号无效");
        }
        String userPhoneLastFour = userPhone.substring(userPhone.length() - 4);
        if (!phone.equals(userPhoneLastFour)) {
            return Result.failure("手机号后四位验证失败");
        }

        // 验证身份证号
        if (!idNum.equals(userIdNum)) {
            return Result.failure("身份证号验证失败");
        }

        // 验证通过
        return Result.success(certEntity, "验证成功", 200);
    }


    @Override
    public Boolean validateCaptcha(String captchaKey, String userInput) {
        // 从Redis中获取验证码
        String storedCaptcha = redisTemplate.opsForValue().get(captchaKey);

        // 验证用户输入的验证码是否与Redis中的验证码一致
        if (storedCaptcha != null && storedCaptcha.equalsIgnoreCase(userInput)) {
            // 验证成功后删除Redis中的验证码
            redisTemplate.delete(captchaKey);
            return true;
        } else {
            return false;
        }
    }
}