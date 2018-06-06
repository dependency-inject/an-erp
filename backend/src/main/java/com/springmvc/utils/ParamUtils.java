package com.springmvc.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParamUtils {

    /**
     * json转泛型列表
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToList(String json, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
        try {
            return (List<T>) mapper.readValue(json, javaType);
        } catch (IOException e) {
            return new ArrayList<T>();
        }
    }

    /**
     * 字符串转date
     * @param time
     * @return
     */
    public static Date toDate(Long time) {
        if (time == -1) {
            return null;
        }
        return new Date(time);
    }

    /**
     * 字符串转int列表
     * @param str
     * @return
     */
    public static List<Integer> toIntList(String str) {
        return toIntList(str, ",");
    }

    /**
     * 字符串转int列表
     * @param str
     * @param delimiter
     * @return
     */
    public static List<Integer> toIntList(String str, String delimiter) {
        String[] splitResult = str.split(delimiter);
        List<Integer> list = new ArrayList<Integer>();
        for (String s : splitResult) {
            if (!isNull(s)) {
                list.add(Integer.parseInt(s));
            }
        }

        return list;
    }

    /**
     * 字符串转str列表
     * @param str
     * @return
     */
    public static List<String> toStrList(String str) {
        return toStrList(str, ",");
    }

    /**
     * 字符串转str列表
     * @param str
     * @param delimiter
     * @return
     */
    public static List<String> toStrList(String str, String delimiter) {
        String[] splitResult = str.split(delimiter);
        return Arrays.asList(splitResult);
    }

    public static <T> String toStr(Iterable<T> elements) {
        return toStr(elements, ",");
    }

    public static <T> String toStr(Iterable<T> elements, String delimiter) {
        if (!elements.iterator().hasNext()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (T element : elements) {
            sb.append(element).append(delimiter);
        }
        return sb.substring(0, sb.lastIndexOf(delimiter));
    }

    /**
     * 驼峰转下划线
     * @param str
     * @return
     */
    public static String camel2Underline(String str) {
        if (str == null) {
            return null;
        }
        str = String.valueOf(str.charAt(0)).toUpperCase().concat(str.substring(1));
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toUpperCase());
            sb.append(matcher.end() == str.length() ? "" : "_");
        }
        return sb.toString();
    }

    /**
     * 判断是否为NULL或者空字符串
     * @param object
     * @return
     */
    public static boolean isNull(Object object) {
        if (object == null) {
            return true;
        }
        return object instanceof String && ((String) object).trim().equals("");
    }

    /**
     * 日期时间格式化
     */
    public static String dateConvert(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
}
