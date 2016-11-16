package com.example.administrator.chotot.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 09/11/2016.
 */

public class StringUtils {
    public static String removeAccent(String s) {

        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }
}
