package com.springmvc.utils;

import java.util.ArrayList;
import java.util.List;

public class ParamUtils {

    public static List<Integer> strToIntList(String str) {
        return strToIntList(str, ",");
    }

    public static List<Integer> strToIntList(String str, String seperator) {
        String[] splitResult = str.split(seperator);
        List<Integer> list = new ArrayList<Integer>();
        for (String s : splitResult) {
            list.add(Integer.parseInt(s));
        }
        return list;
    }

    public static boolean isNull(Object object) {
        if (object == null) {
            return true;
        }
        return object instanceof String && ((String) object).trim().equals("");
    }
}
