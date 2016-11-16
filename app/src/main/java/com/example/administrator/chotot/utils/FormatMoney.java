package com.example.administrator.chotot.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Administrator on 21/10/2016.
 */

public class FormatMoney {
    public static String format(int number){
        if(number < 1000){
            return String.valueOf(number);
        }
        try{
            NumberFormat format = new DecimalFormat("###,###");
            String resp = format.format(number);
            resp = resp.replaceAll(",", ".");
            return resp;
        }catch (Exception e){
            return "";
        }
    }
}
