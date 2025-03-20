/**
 * @Author: wangbo 3812943352@qq.com
 * @Date: 2024-12-07 14:31:52
 * @LastEditors: wangbo 3812943352@qq.com
 * @LastEditTime: 2024-12-07 14:32:06
 * @FilePath: common-module/src/main/java/com/common/commonmodule/util/SizeFormatter.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package com.common.commonmodule.util;

import java.text.DecimalFormat;

public class SizeFormatter {

    private static final long KB = 1024;
    private static final long MB = KB * 1024;
    private static final long GB = MB * 1024;

    public static String formatSize(long size) {
        if (size < KB) {
            return size + " B";
        } else if (size < MB) {
            return formatDecimal(size / (double) KB) + " KB";
        } else if (size < GB) {
            return formatDecimal(size / (double) MB) + " MB";
        } else {
            return formatDecimal(size / (double) GB) + " GB";
        }
    }

    private static String formatDecimal(double value) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(value);
    }
}