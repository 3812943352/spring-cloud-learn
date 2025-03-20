/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-13 14:16:00
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-13 14:45:23
 * @FilePath: ApiSupervision-service/src/main/java/org/learning/apisupervisionservice/service/impl/GatewayServiceImpl.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.apisupervisionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.commonmodule.resp.Result;
import org.learning.apisupervisionservice.entity.GatewayEntity;
import org.learning.apisupervisionservice.mapper.GatewayMapper;
import org.learning.apisupervisionservice.service.GatewayService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 王博
 * @since 2025-03-13
 */
@Service
public class GatewayServiceImpl extends ServiceImpl<GatewayMapper, GatewayEntity> implements GatewayService {


    @Override
    public Result<?> getAll(int pageNum, int pageSize) {
        Page<GatewayEntity> resultPage = new Page<>(pageNum, pageSize);


        resultPage = this.baseMapper.selectPageWithApiTitle(resultPage);

        return Result.success(resultPage, "请求成功");
    }


    @Override
    public Result<?> getDate(int pageNum, int pageSize, long startTime, long endTime) {
        Page<GatewayEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<GatewayEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("req_time", startTime, endTime);
        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage);
    }

    @Override
    public Result<?> getBlur(int pageNum, int pageSize, String word) {
        Page<GatewayEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<GatewayEntity> queryWrapper = new QueryWrapper<>();
        if (word != null && !word.isEmpty()) {
            queryWrapper.or(wrapper -> wrapper
                    .like("username", word)
                    .or().like("req_ip", word)
                    .or().like("req_name", word)
                    .or().like("req_code", word)
                    .or().like("errorinfo", word)
            );
        }
        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage, "请求成功");

    }

    @Override
    public Result<?> del(int id) {
        try {
            this.removeById(id);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.failure(202, "删除失败: " + e.getMessage());
        }

    }
}
