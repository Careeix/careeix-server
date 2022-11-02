package com.example.careeix.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationRegex {

    public static boolean isRegexNickname(String target) {
        String regex = "^[a-z0-9가-힣]{2,10}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    public static boolean isRegexNicknameNum(String target) {
        String regex = "^[0-9]{2,10}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }
}

