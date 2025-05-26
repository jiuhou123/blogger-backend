package com.jiuhou.common.domain;

import lombok.Data;

/**
 * 通用返回对象
 */
@Data
public class R<T> {
    /**
     * 状态码
     */
    private int code;

    /**
     * 返回信息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    private R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功返回结果
     */
    public static <T> R<T> ok(T data) {
        return new R<>(200, "操作成功", data);
    }

    /**
     * 成功返回结果
     */
    public static <T> R<T> ok(String msg, T data) {
        return new R<>(200, msg, data);
    }

    /**
     * 失败返回结果
     */
    public static <T> R<T> fail(String msg) {
        return new R<>(500, msg, null);
    }

    /**
     * 失败返回结果
     */
    public static <T> R<T> fail(int code, String msg) {
        return new R<>(code, msg, null);
    }
}