package com.centralway.player.utils;

/**
 * Created by sergiodejesus on 6/24/17.
 */

public class TimeUtils {

    public static String getTimeFormatted(long ms) {
        StringBuilder result = new StringBuilder();
        long seconds = ms / 1000;
        long minute = seconds / 60;
        seconds = seconds - (minute * 60);
        result.append(minute > 9 ? String.valueOf(minute) : "0" + String.valueOf(minute));
        result.append(" : ");
        result.append(seconds > 9 ? String.valueOf(seconds) : "0" + String.valueOf(seconds));
        return result.toString();
    }
}
