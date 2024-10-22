package com.jas.agent;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 基于jackson封装的json util, 主要方法 toJson, fromJson
 *
 * @author chengleiwang
 */
public final class InnerLogUtils {
    static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    public static void info(Object... args) {
        args = args == null ? new Object[0] : args;
        StringBuilder builder = new StringBuilder();
        for (Object arg : args) {
            builder.append(arg).append("  ");
        }
        info(builder.toString());
    }

    public static void info(String str) {
        System.out.printf("[%s] [%s] [log_agent] %s%n", getTimestamp(), getThreadName(), str);
    }

    public static void error(String str) {
        System.err.printf("[%s] [%s] [log_agent] %s%n", getTimestamp(), getThreadName(), str);
    }

    public static void error(String str, Throwable ex) {
        System.err.printf("[%s] [%s] [log_agent] %s %s%n", getTimestamp(), getThreadName(), str, getStackTrace(ex));
    }

    public static String getTimestamp() {
        return LocalDateTime.now().format(TIME_FORMATTER);
    }

    public static String getThreadName() {
        return Thread.currentThread().getName();
    }
}
