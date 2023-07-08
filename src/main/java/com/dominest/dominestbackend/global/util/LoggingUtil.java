package com.dominest.dominestbackend.global.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class LoggingUtil {
    public static String stackTraceToString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw); // 반환값용 로그 남기기
        return sw.toString();
    }
}
