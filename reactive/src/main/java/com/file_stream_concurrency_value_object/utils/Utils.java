package com.file_stream_concurrency_value_object.utils;

public class Utils {

    public static double roundOne(double number) {
        return Math.round(number * 10.0) / 10.0;
    }

    public static double roundTwo(double number) {
        return Math.round(number * 100.0) / 100.0;
    }
}
