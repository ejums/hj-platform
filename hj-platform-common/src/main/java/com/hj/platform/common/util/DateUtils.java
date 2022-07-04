package com.hj.platform.common.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Locale;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
public class DateUtils {
    public static final DateTimeFormatter FORMAT_TIME = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.CHINA);
    public static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.CHINA);
    public static final DateTimeFormatter FORMAT_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    private static final ZoneOffset LOCAL_ZONE =  ZoneOffset.of("+8");

    protected DateUtils() throws InstantiationException {}


    public static String stringify(DateTimeFormatter formatter, TemporalAccessor temporalAccessor){
        return formatter.format(temporalAccessor);
    }

    public static String stringifyDate(TemporalAccessor temporalAccessor){
        return FORMAT_DATE.format(temporalAccessor);
    }

    public static String stringifyDateTime(TemporalAccessor temporalAccessor){
        return FORMAT_DATE_TIME.format(temporalAccessor);
    }

    public static String stringifyTime(TemporalAccessor temporalAccessor){
        return FORMAT_TIME.format(temporalAccessor);
    }

    public static LocalDateTime parserDateTime(String text){
        return LocalDateTime.parse(text, FORMAT_DATE_TIME);
    }

    public static LocalDate parserDate(String text) {
        return LocalDate.parse(text, FORMAT_DATE);
    }

    public static LocalTime parserTime(String text){
        return LocalTime.parse(text, FORMAT_TIME);
    }


    public static LocalDateTime convert(Date date){
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static Date convert(LocalDateTime date){
        return Date.from(date.toInstant(LOCAL_ZONE));
    }

    public static long getTime(LocalDateTime date){
        return date.toEpochSecond(LOCAL_ZONE);
    }

    public static long getTime(Date date){
        return date.getTime();
    }
}
