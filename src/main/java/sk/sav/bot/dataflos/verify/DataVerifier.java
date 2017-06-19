/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.verify;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Jakub
 */
public class DataVerifier {
    
    public static boolean isNadmorskaVyska(String value) {
        return (isIntNumber(value) && Integer.parseInt(value)>-7000 && Integer.parseInt(value)<9000);
    }
    
    public static boolean isKvadrant(String value) {
        final Set<String> kvadrantLetter = new HashSet<>(Arrays.asList("a", "b", "c", "d"));
        while (true){
            if (value.length() >= 6){
                if (isIntNumber(String.valueOf(value.charAt(0))) && isIntNumber(String.valueOf(value.charAt(1))) && isIntNumber(String.valueOf(value.charAt(2))) && isIntNumber(String.valueOf(value.charAt(3))) && isIntNumber(String.valueOf(value.charAt(4))) && kvadrantLetter.contains(String.valueOf(value.charAt(5)))){
                    value = value.substring(6, value.length());
                    if (value.equals("")){
                        return true;
                    } else if (String.valueOf(value.charAt(0)).equals("/")) {
                        value = value.substring(1, value.length());
                    }
                } else {
                   return false;
                }
            } else {
                return false;
            }
        }     
    }

    public static boolean isZemepisnaPoloha(String value) {
        if (value.indexOf('°', 0)>-1 && value.indexOf('\'', 0)>-1 && value.indexOf("\'\'", 0)>-1){
            String deg = value.substring(0, value.indexOf('°'));
            String min = value.substring(value.indexOf('°')+1, value.indexOf('\''));
            String sec = value.substring(value.indexOf('\'')+1, value.indexOf("\'\'"));
            return isIntNumber(deg) && isIntNumber(min) && isDoubleNumber(sec);
        } else {
            return false;
        }
    }

    public static boolean isCorrectDateStamp(String value, boolean untilNow) {
        if (value.length() == 8){
            String year = value.substring(0, 4);
            String month = value.substring(4, 6);
            String day = value.substring(6, 8);
            if (isIntNumber(year) && isIntNumber(month) && isIntNumber(day)){
                int intYear = Integer.parseInt(year);
                Calendar cal = new GregorianCalendar();
                int intMonth = Integer.parseInt(month);
                int intDay = Integer.parseInt(day);
                boolean isYear = true;
                if (untilNow){
                    isYear = (intYear >= 0 && intYear <= cal.get(Calendar.YEAR));
                }
                boolean isMonth = (intMonth >= 0 && intMonth <= 12); //ak sa presny mesiac/den nevie moze sa uviest aj 00
                boolean isDay = (intDay >= 0 && intDay <= 31);

                return isYear && isMonth && isDay;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    public static boolean isYear(String year) {
        if (isIntNumber(year)){
            int intYear = Integer.parseInt(year);
            Calendar cal = new GregorianCalendar();
            return (intYear >= 0 && intYear <= cal.get(Calendar.YEAR));
        } else {
            return false;
        }
    }
    
    public static boolean isIntNumber(String str)  
    {  
        try
        {  
          int i = Integer.parseInt(str);  
        }  
        catch(NumberFormatException nfe)  
        {  
          return false;  
        }  
        return true;  
    }
    
    public static boolean isDoubleNumber(String str)  
    {  
        try
        {  
          double i = Double.parseDouble(str);  
        }  
        catch(NumberFormatException nfe)  
        {  
          return false;  
        }  
        return true;  
    }
    
}
