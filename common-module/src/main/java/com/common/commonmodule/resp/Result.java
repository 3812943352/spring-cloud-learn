package com.common.commonmodule.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Builder(toBuilder = true)
@AllArgsConstructor
@Setter
@Getter
@Slf4j
public class Result<T> {
    /**
     * 提示信息
     */
    @Schema(description = "提示信息")
    private T message;
    /**
     * 提示信息
     */
    @Schema(description = "多个提示信息")
    private T messages;
    /**
     * 是否成功
     */
    @Schema(description = "是否成功")
    private boolean success;
    /**
     * 返回状态码
     */
    @Schema(description = "返回状态码")
    private Integer code;
    /**
     * 数据
     */
    @Schema(description = "数据")
    private T data;

    public Result() {
    }

    public static Result success() {
        Result Result = new Result();
        Result.setSuccess(Boolean.TRUE);
        Result.setCode(ResultCode.SUCCESS.getCode());
        Result.setMessage(ResultCode.SUCCESS.getMsg());
        return Result;
    }

    public static Result success(String msg) {
        Result Result = new Result();
        Result.setMessage(msg);
        Result.setSuccess(Boolean.TRUE);
        Result.setCode(ResultCode.SUCCESS.getCode());

        return Result;
    }

    public static Result success(Object data) {
        Result Result = new Result();
        Result.setData(data);
        Result.setSuccess(Boolean.TRUE);
        Result.setCode(ResultCode.SUCCESS.getCode());
        Result.setMessage(ResultCode.SUCCESS.getMsg());
        return Result;
    }

    public static Result success(Object data, String msg) {
        Result Result = new Result();
        Result.setData(data);
        Result.setMessage(msg);
        Result.setSuccess(Boolean.TRUE);
        Result.setCode(ResultCode.SUCCESS.getCode());
        Result.setMessage(ResultCode.SUCCESS.getMsg());
        return Result;
    }

    public static <T> Result<T> success(T data, T msg, int code) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(msg);
        result.setData(data);
        return result;
    }

    /**
     * 返回失败 消息
     *
     * @return Result
     */
    public static Result failure() {
        Result Result = new Result();
        Result.setSuccess(Boolean.FALSE);
        Result.setCode(ResultCode.FAILURE.getCode());
        Result.setMessage(ResultCode.FAILURE.getMsg());
        return Result;
    }

    /**
     * 返回失败 消息
     *
     * @param msg 失败信息
     * @return Result
     */
    public static Result failure(String msg) {
        Result Result = new Result();
        Result.setSuccess(Boolean.FALSE);
        Result.setCode(ResultCode.FAILURE.getCode());
        Result.setMessage(msg);
        return Result;
    }

    public static Result failure(Integer code, String msg) {
        Result Result = new Result();
        Result.setSuccess(Boolean.FALSE);
        Result.setCode(code);
        Result.setMessage(msg);
        return Result;
    }


    public static Result failure(String msg, ResultCode exceptionCode) {
        Result Result = new Result();
        Result.setMessage(msg);
        Result.setSuccess(Boolean.FALSE);
        Result.setCode(exceptionCode.getCode());
        Result.setData(exceptionCode.getMsg());
        return Result;
    }

    /**
     * 返回失败 消息
     *
     * @param exceptionCode 错误信息枚举
     * @return Result
     */
    public static Result failure(ResultCode exceptionCode) {
        Result Result = new Result();
        Result.setSuccess(Boolean.FALSE);
        Result.setCode(exceptionCode.getCode());
        Result.setMessage(exceptionCode.getMsg());
        return Result;
    }

    /**
     * 返回失败 消息
     *
     * @param exceptionCode 错误信息枚举
     * @param msg           自定义错误提示信息
     * @return Result
     */
    public static Result failure(ResultCode exceptionCode, String msg) {
        Result Result = new Result();
        Result.setMessage(msg);
        Result.setSuccess(Boolean.FALSE);
        Result.setCode(exceptionCode.getCode());
        return Result;
    }

    public static Result<?> failure(Integer exceptionCode, List<String> message) {
        Result Result = new Result();
        Result.setCode(exceptionCode);
        Result.setMessages(message);
        return Result;
    }
}
