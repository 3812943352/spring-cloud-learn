/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-12 16:53:42
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-13 16:25:41
 * @FilePath: ApiSupervision-service/src/main/java/org/learning/apisupervisionservice/service/impl/ApiServiceImpl.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.apisupervisionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.commonmodule.resp.Result;
import org.learning.apisupervisionservice.entity.ApiEntity;
import org.learning.apisupervisionservice.entity.GatewayEntity;
import org.learning.apisupervisionservice.mapper.ApiMapper;
import org.learning.apisupervisionservice.mapper.GatewayMapper;
import org.learning.apisupervisionservice.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 王博
 * @since 2025-03-12
 */
@Service
public class ApiServiceImpl extends ServiceImpl<ApiMapper, ApiEntity> implements ApiService {

    @Autowired
    private ApiMapper apiMapper;
    @Autowired
    private GatewayMapper gatewayMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    private static List<Long> getLastNMinutesTimestamps(Instant now, int n) {
        List<Long> timestamps = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Instant pastMinute = now.minus(i, ChronoUnit.MINUTES);
            timestamps.add(pastMinute.getEpochSecond());
        }
        // Reverse the list to get from oldest to newest
        java.util.Collections.reverse(timestamps);
        return timestamps;
    }

    private static List<Long> getLastNHourseTimestamps(Instant now, int n) {
        List<Long> timestamps = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Instant pastHour = now.minus(i, ChronoUnit.HOURS);
            timestamps.add(pastHour.getEpochSecond());
        }
        // Reverse the list to get from oldest to newest
        java.util.Collections.reverse(timestamps);
        return timestamps;
    }

    private static List<Long> getLastNDaysTimestamps(Instant now, int n) {
        List<Long> timestamps = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Instant pastDay = now.minus(i, ChronoUnit.DAYS);
            timestamps.add(pastDay.getEpochSecond());
        }
        // Reverse the list to get from oldest to newest
        java.util.Collections.reverse(timestamps);
        return timestamps;
    }

    @Override
    public Result<?> homeData() {
        Map<String, Object> result = new HashMap<>();
        QueryWrapper<ApiEntity> queryWrapper = Wrappers.<ApiEntity>query()
                .select("ID");
        Long apiCount = apiMapper.selectCount(queryWrapper);
        result.put("apiCount", apiCount);
        QueryWrapper<ApiEntity> timesSumWrapper = Wrappers.<ApiEntity>query()
                .select("SUM(times) as total_times");
        List<Map<String, Object>> resultMaps = apiMapper.selectMaps(timesSumWrapper);
        Long totalTimes = resultMaps.isEmpty() ? 0 : ((Number) resultMaps.get(0).get("total_times")).longValue();
        result.put("totalTimes", totalTimes);
        QueryWrapper<ApiEntity> apiTop10Wrapper = Wrappers.<ApiEntity>query()
                .select("api_title", "times")
                .orderByDesc("times")
                .last("LIMIT 10");
        List<Map<String, Object>> apiTop10 = apiMapper.selectMaps(apiTop10Wrapper);
        List<List<Object>> apiTop = new ArrayList<>();
        apiTop10.forEach(map -> {
            List<Object> api = new ArrayList<>();
            api.add(map.get("api_title"));
            api.add(map.get("times"));
            apiTop.add(api);
        });
        result.put("apiTop10", apiTop);
        return Result.success(result);
    }

    @Override
    public Result<?> getLine() {
        Instant now = Instant.now();
        Map<String, Object> result = new HashMap<>();

        // 获取过去60分钟每一分钟的Unix时间戳
        List<Long> last60MinutesTimestamps = getLastNMinutesTimestamps(now, 60);
        // 获取过去24小时每一小时的Unix时间戳
        List<Long> last24HoursTimestamps = getLastNHourseTimestamps(now, 24);
        // 获取过去30天每一天的Unix时间戳
        List<Long> last30DaysTimestamps = getLastNDaysTimestamps(now, 30);
        List<List<Object>> mReqCount = new ArrayList<>();
        List<List<Object>> mPerMs = new ArrayList<>();
        List<List<Object>> hReqCount = new ArrayList<>();
        List<List<Object>> hPerMs = new ArrayList<>();
        List<List<Object>> dReqCount = new ArrayList<>();
        List<List<Object>> dPerMs = new ArrayList<>();
        ZoneId zoneId = ZoneId.systemDefault();
        for (int i = 0; i < last60MinutesTimestamps.size(); i++) {
            if (i == 0) {
                QueryWrapper<GatewayEntity> mReqWrapper = Wrappers.<GatewayEntity>query()
                        .between("req_time", last60MinutesTimestamps.get(i), System.currentTimeMillis() / 1000);
                int Count = Math.toIntExact(gatewayMapper.selectCount(mReqWrapper));
                List<Map<String, Object>> mPer = gatewayMapper.selectMaps(
                        Wrappers.<GatewayEntity>query()
                                .between("req_time", last60MinutesTimestamps.get(i), System.currentTimeMillis() / 1000)
                                .select("ROUND(AVG(ms), 0) as avg_ms"));
                Instant instant = Instant.ofEpochSecond(last60MinutesTimestamps.get(i));
                DateTimeFormatter minuteFormatter = DateTimeFormatter.ofPattern("mm").withZone(zoneId);
                String minute = minuteFormatter.format(instant);
                List<Object> pair0 = new ArrayList<>();
                Object value = mPer.get(0) == null ? 0 : mPer.get(0).get("avg_ms");
                pair0.add(minute);
                pair0.add(value);
                mPerMs.add(pair0);
                List<Object> pair = new ArrayList<>();
                pair.add(minute);
                pair.add(Count);
                mReqCount.add(pair);
            } else {
                QueryWrapper<GatewayEntity> mReqWrapper = Wrappers.<GatewayEntity>query()
                        .between("req_time", last60MinutesTimestamps.get(i - 1), last60MinutesTimestamps.get(i));
                int Count = Math.toIntExact(gatewayMapper.selectCount(mReqWrapper));
                List<Map<String, Object>> mPer = gatewayMapper.selectMaps(
                        Wrappers.<GatewayEntity>query()
                                .between("req_time", last60MinutesTimestamps.get(i - 1), last60MinutesTimestamps.get(i))
                                .select("ROUND(AVG(ms), 0) as avg_ms"));
                Instant instant = Instant.ofEpochSecond(last60MinutesTimestamps.get(i));
                DateTimeFormatter minuteFormatter = DateTimeFormatter.ofPattern("mm").withZone(zoneId);
                String minute = minuteFormatter.format(instant);

                List<Object> pair0 = new ArrayList<>();
                Object value = mPer.get(0) == null ? 0 : mPer.get(0).get("avg_ms");
                pair0.add(minute);
                pair0.add(value);
                mPerMs.add(pair0);
                List<Object> pair = new ArrayList<>();
                pair.add(minute);
                pair.add(Count);
                mReqCount.add(pair);
            }
        }
        result.put("mReqCount", mReqCount);
        result.put("mPerMs", mPerMs);
        for (int i = 0; i < last24HoursTimestamps.size(); i++) {
            if (i == 0) {
                QueryWrapper<GatewayEntity> mReqWrapper = Wrappers.<GatewayEntity>query()
                        .between("req_time", last24HoursTimestamps.get(i), System.currentTimeMillis() / 1000);
                int Count = Math.toIntExact(gatewayMapper.selectCount(mReqWrapper));
                List<Map<String, Object>> mPer = gatewayMapper.selectMaps(
                        Wrappers.<GatewayEntity>query()
                                .between("req_time", last24HoursTimestamps.get(i), System.currentTimeMillis() / 1000)
                                .select("ROUND(AVG(ms), 0) as avg_ms"));
                Instant instant = Instant.ofEpochSecond(last24HoursTimestamps.get(i));
                DateTimeFormatter minuteFormatter = DateTimeFormatter.ofPattern("HH").withZone(zoneId);
                String minute = minuteFormatter.format(instant);
                List<Object> pair0 = new ArrayList<>();
                Object value = mPer.get(0) == null ? 0 : mPer.get(0).get("avg_ms");
                pair0.add(minute);
                pair0.add(value);
                hPerMs.add(pair0);
                List<Object> pair = new ArrayList<>();
                pair.add(minute);
                pair.add(Count);
                hReqCount.add(pair);
            } else {
                QueryWrapper<GatewayEntity> mReqWrapper = Wrappers.<GatewayEntity>query()
                        .between("req_time", last24HoursTimestamps.get(i - 1), last24HoursTimestamps.get(i));
                int Count = Math.toIntExact(gatewayMapper.selectCount(mReqWrapper));
                List<Map<String, Object>> mPer = gatewayMapper.selectMaps(
                        Wrappers.<GatewayEntity>query()
                                .between("req_time", last24HoursTimestamps.get(i - 1), last24HoursTimestamps.get(i))
                                .select("ROUND(AVG(ms), 0) as avg_ms"));
                Instant instant = Instant.ofEpochSecond(last24HoursTimestamps.get(i));
                DateTimeFormatter minuteFormatter = DateTimeFormatter.ofPattern("HH").withZone(zoneId);
                String minute = minuteFormatter.format(instant);

                List<Object> pair0 = new ArrayList<>();
                Object value = mPer.get(0) == null ? 0 : mPer.get(0).get("avg_ms");
                pair0.add(minute);
                pair0.add(value);
                hPerMs.add(pair0);
                List<Object> pair = new ArrayList<>();
                pair.add(minute);
                pair.add(Count);
                hReqCount.add(pair);
            }
        }
        result.put("hReqCount", hReqCount);
        result.put("hPerMs", hPerMs);
        for (int i = 0; i < last30DaysTimestamps.size(); i++) {
            if (i == 0) {
                QueryWrapper<GatewayEntity> mReqWrapper = Wrappers.<GatewayEntity>query()
                        .between("req_time", last30DaysTimestamps.get(i), System.currentTimeMillis() / 1000);
                int Count = Math.toIntExact(gatewayMapper.selectCount(mReqWrapper));
                List<Map<String, Object>> mPer = gatewayMapper.selectMaps(
                        Wrappers.<GatewayEntity>query()
                                .between("req_time", last30DaysTimestamps.get(i), System.currentTimeMillis() / 1000)
                                .select("ROUND(AVG(ms), 0) as avg_ms"));
                Instant instant = Instant.ofEpochSecond(last30DaysTimestamps.get(i));
                DateTimeFormatter minuteFormatter = DateTimeFormatter.ofPattern("dd").withZone(zoneId);
                String minute = minuteFormatter.format(instant);
                List<Object> pair0 = new ArrayList<>();
                Object value = mPer.get(0) == null ? 0 : mPer.get(0).get("avg_ms");
                pair0.add(minute);
                pair0.add(value);
                dPerMs.add(pair0);
                List<Object> pair = new ArrayList<>();
                pair.add(minute);
                pair.add(Count);
                dReqCount.add(pair);
            } else {
                QueryWrapper<GatewayEntity> mReqWrapper = Wrappers.<GatewayEntity>query()
                        .between("req_time", last30DaysTimestamps.get(i - 1), last30DaysTimestamps.get(i));
                int Count = Math.toIntExact(gatewayMapper.selectCount(mReqWrapper));
                List<Map<String, Object>> mPer = gatewayMapper.selectMaps(
                        Wrappers.<GatewayEntity>query()
                                .between("req_time", last30DaysTimestamps.get(i - 1), last30DaysTimestamps.get(i))
                                .select("ROUND(AVG(ms), 0) as avg_ms"));
                Instant instant = Instant.ofEpochSecond(last30DaysTimestamps.get(i));
                DateTimeFormatter minuteFormatter = DateTimeFormatter.ofPattern("dd").withZone(zoneId);
                String minute = minuteFormatter.format(instant);

                List<Object> pair0 = new ArrayList<>();
                Object value = mPer.get(0) == null ? 0 : mPer.get(0).get("avg_ms");
                pair0.add(minute);
                pair0.add(value);
                dPerMs.add(pair0);
                List<Object> pair = new ArrayList<>();
                pair.add(minute);
                pair.add(Count);
                dReqCount.add(pair);
            }
        }
        result.put("dReqCount", dReqCount);
        result.put("dPerMs", dPerMs);
        return Result.success(result);
    }

    @Override
    public Result<?> getCharts(long start, long end) {
        Map<String, Object> result = new HashMap<>();
        List<List<Object>> stuPie = new ArrayList<>();
        String[] stu = {"200", "500", "404", "401", "403"};
        if (start == 0 && end == 0) {
            for (int i = 0; i < stu.length; i++) {
                QueryWrapper<GatewayEntity> stuWrapper = Wrappers.<GatewayEntity>query()
                        .eq("req_code", stu[i]);
                int stuCount = Math.toIntExact(gatewayMapper.selectCount(stuWrapper));

                List<Object> stuPies = new ArrayList<>();
                stuPies.add(stu[i]);
                stuPies.add(stuCount);
                stuPie.add(stuPies);
            }
            result.put("stuPie", stuPie);
        } else {
            for (int i = 0; i < stu.length; i++) {
                QueryWrapper<GatewayEntity> stuWrapper = Wrappers.<GatewayEntity>query()
                        .between("req_time", start, end == 0 ? System.currentTimeMillis() / 1000 : end)
                        .eq("req_code", stu[i]);
                int stuCount = Math.toIntExact(gatewayMapper.selectCount(stuWrapper));

                List<Object> stuPies = new ArrayList<>();
                stuPies.add(stu[i]);
                stuPies.add(stuCount);
                stuPie.add(stuPies);
            }
            result.put("stuPie", stuPie);
        }


        List<List<Object>> msPie = new ArrayList<>();
        int[] msRange = {0, 100, 500, 1000, 3000, 5000};
        if (start == 0 && end == 0) {
            for (int i = 0; i < msRange.length; i++) {
                if (i == 5) {
                    QueryWrapper<GatewayEntity> msWrapper = Wrappers.<GatewayEntity>query()
                            .between("ms", msRange[i], 999999999);
                    int msCount = Math.toIntExact(gatewayMapper.selectCount(msWrapper));
                    List<Object> msPies = new ArrayList<>();
                    msPies.add(msRange[i] + "ms" + "以上");
                    msPies.add(msCount);
                    msPie.add(msPies);
                } else {
                    QueryWrapper<GatewayEntity> msWrapper = Wrappers.<GatewayEntity>query()
                            .between("ms", msRange[i], msRange[i + 1]);
                    int msCount = Math.toIntExact(gatewayMapper.selectCount(msWrapper));
                    List<Object> msPies = new ArrayList<>();
                    msPies.add(msRange[i] + "ms" + "-" + msRange[i + 1] + "ms");
                    msPies.add(msCount);
                    msPie.add(msPies);
                }
            }
        } else {
            for (int i = 0; i < msRange.length; i++) {
                if (i == 5) {
                    QueryWrapper<GatewayEntity> msWrapper = Wrappers.<GatewayEntity>query()
                            .between("req_time", start, end == 0 ? System.currentTimeMillis() / 1000 : end)
                            .between("ms", msRange[i], 999999999);
                    int msCount = Math.toIntExact(gatewayMapper.selectCount(msWrapper));
                    List<Object> msPies = new ArrayList<>();
                    msPies.add(msRange[i] + "ms" + "以上");
                    msPies.add(msCount);
                    msPie.add(msPies);
                } else {
                    QueryWrapper<GatewayEntity> msWrapper = Wrappers.<GatewayEntity>query()
                            .between("req_time", start, end == 0 ? System.currentTimeMillis() / 1000 : end)
                            .between("ms", msRange[i], msRange[i + 1]);
                    int msCount = Math.toIntExact(gatewayMapper.selectCount(msWrapper));
                    List<Object> msPies = new ArrayList<>();
                    msPies.add(msRange[i] + "ms" + "-" + msRange[i + 1] + "ms");
                    msPies.add(msCount);
                    msPie.add(msPies);
                }
            }
        }

        result.put("msPie", msPie);
        List<List<Object>> errorTop15 = new ArrayList<>();
        List<List<Object>> reqTop15 = new ArrayList<>();

        List<Map<String, Object>> errorTop = apiMapper.selectMaps(
                Wrappers.<ApiEntity>query().orderByDesc("errors").last("LIMIT 15")
        );
        errorTop.forEach(map -> {
            List<Object> errorPies = new ArrayList<>();
            errorPies.add(map.get("api"));
            errorPies.add(map.get("errors"));
            errorTop15.add(errorPies);
        });
        result.put("errorTop15", errorTop15);
        List<Map<String, Object>> reqTop = apiMapper.selectMaps(
                Wrappers.<ApiEntity>query().orderByDesc("times").last("LIMIT 15")
        );
        reqTop.forEach(map -> {
            List<Object> reqPies = new ArrayList<>();
            reqPies.add(map.get("api"));
            reqPies.add(map.get("times"));
            reqTop15.add(reqPies);
        });
        result.put("reqTop15", reqTop15);


        List<String> apiList = apiMapper.getAllApi();
        List<List<Object>> fastTop15 = new ArrayList<>();
        List<List<Object>> slowTop15 = new ArrayList<>();
        String msKey = "ms";
        for (String api : apiList) {
            List<Map<String, Object>> records = gatewayMapper.selectMaps(
                    Wrappers.<GatewayEntity>query()
                            .eq("req_name", api)

            );
            OptionalDouble avgMsOptional = records.stream()
                    .filter(map -> map.containsKey(msKey))
                    .mapToLong(map -> ((Number) map.get(msKey)).longValue())
                    .average();

            List<List<Object>> finalFastTop = fastTop15;
            List<List<Object>> finalSlowTop = slowTop15;

            avgMsOptional.ifPresent(avgMs -> {
                BigDecimal bd = new BigDecimal(Double.toString(avgMs));
                bd = bd.setScale(2, RoundingMode.HALF_UP);
                finalFastTop.add(Arrays.asList(api, bd.doubleValue()));
            });
            avgMsOptional.ifPresent(avgMs -> {
                BigDecimal bd = new BigDecimal(Double.toString(avgMs));
                bd = bd.setScale(2, RoundingMode.HALF_UP);
                finalSlowTop.add(Arrays.asList(api, bd.doubleValue()));
            });
        }
        fastTop15 = fastTop15.stream()
                .sorted(Comparator.comparing((List<Object> entry) -> (Double) entry.get(1)))
                .limit(Math.min(15, fastTop15.size()))
                .collect(Collectors.toList());
        slowTop15 = slowTop15.stream()
                .sorted(Comparator.comparing((List<Object> entry) -> (Double) entry.get(1), Comparator.reverseOrder()))
                .limit(Math.min(15, slowTop15.size()))
                .collect(Collectors.toList());
        result.put("fastTop15", fastTop15);
        result.put("slowTop15", slowTop15);

        return Result.success(result);
    }

    @Override
    public Result<?> addApi(ApiEntity apiEntity) {
        String api = apiEntity.getApi();
        if (this.getOne(new QueryWrapper<ApiEntity>().eq("api", api)) != null) {
            return Result.failure("请勿添加重复API");
        }
        redisTemplate.delete("authList");
        redisTemplate.delete("loginList");
        try {
            List<Map<String, Object>> outh = apiMapper.selectMaps(
                    Wrappers.<ApiEntity>query().eq("auth", "用户")
            );
            List<Map<String, Object>> openMethod = apiMapper.selectMaps(
                    Wrappers.<ApiEntity>query().eq("open_method", "无限制")
            );
            List<Object> outhList = new ArrayList<>();
            List<Object> openMethodList = new ArrayList<>();
            outhList.add(apiEntity.getApi());
            openMethodList.add(apiEntity.getApi());

            outh.forEach(map -> {
                outhList.add(map.get("api"));
            });
            openMethod.forEach(map -> {
                openMethodList.add(map.get("api"));
            });
            redisTemplate.opsForList().rightPushAll("authList", outhList);
            redisTemplate.opsForList().rightPushAll("loginList", openMethodList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Result.success(this.save(apiEntity));
    }

    @Override
    public Result<?> deleteApi(ApiEntity apiEntity) {
        return Result.success(this.removeById(apiEntity));
    }

    @Override
    public Result<?> updateApi(ApiEntity apiEntity) {
        redisTemplate.delete("authList");
        redisTemplate.delete("loginList");
        try {
            List<Map<String, Object>> outh = apiMapper.selectMaps(
                    Wrappers.<ApiEntity>query().eq("auth", "用户")
            );
            List<Map<String, Object>> openMethod = apiMapper.selectMaps(
                    Wrappers.<ApiEntity>query().eq("open_method", "无限制")
            );
            List<Object> outhList = new ArrayList<>();
            List<Object> openMethodList = new ArrayList<>();
            outh.forEach(map -> {
                outhList.add(map.get("api"));
            });
            openMethod.forEach(map -> {
                openMethodList.add(map.get("api"));
            });
            outhList.add(apiEntity.getApi());
            openMethodList.add(apiEntity.getApi());
            redisTemplate.opsForList().rightPushAll("authList", outhList);
            redisTemplate.opsForList().rightPushAll("loginList", openMethodList);
            System.out.println(openMethodList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Result.success(this.updateById(apiEntity));
    }

    @Override
    public Result<?> getAllApi(int pageNum, int pageSize) {
        Page<ApiEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<ApiEntity> queryWrapper = new QueryWrapper<>();
        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage);
    }

    @Override
    public Result<?> getApiById(Integer id) {
        return Result.success(this.getById(id));
    }

    @Override
    public Result<?> getBlur(int pageNum, int pageSize, String word) {
        Page<ApiEntity> resultPage = new Page<>(pageNum, pageSize);
        QueryWrapper<ApiEntity> queryWrapper = new QueryWrapper<>();
        if (word != null && !word.isEmpty()) {
            queryWrapper.or(wrapper -> wrapper
                    .like("api_title", word)
                    .or().like("api", word)
                    .or().like("api_des", word)
                    .or().like("api_table", word)
            );
        }
        resultPage = this.page(resultPage, queryWrapper);
        return Result.success(resultPage, "请求成功");
    }

}

