package com.easyjava.utils;

import java.util.Locale;

public class StringUtils {
    public static String uperCaseFirstLetter(String field){
        if (org.apache.commons.lang3.StringUtils.isEmpty(field)){
            return field;
        }
        return field.substring(0,1).toUpperCase() + field.substring(1);     // 转为大写
    }

    public static String lowerCaseFirstLetter(String field){
        if (org.apache.commons.lang3.StringUtils.isEmpty(field)){
            return field;
        }
        return field.substring(0,1).toLowerCase() + field.substring(1);     // 转为小写
    }

    public static void main(String[] args) {
        System.out.println(uperCaseFirstLetter("company"));
        System.out.println(lowerCaseFirstLetter("Company"));

    }
}
