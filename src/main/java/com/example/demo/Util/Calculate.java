package com.example.demo.Util;

import org.apache.commons.lang3.ObjectUtils;

import java.text.DecimalFormat;
import java.util.List;

public class Calculate {
    public static float calculateAverage(List<Integer> array) {
        if (ObjectUtils.isEmpty(array)) {
            return 0.0f;
        }
        int sum = 0;
        for (int num : array) {
            sum += num;
        }
        return (float) sum / array.size();
    }

    public static float roundToOneDecimal(float value) {
        DecimalFormat df = new DecimalFormat("#.#");
        return Float.parseFloat(df.format(value));
    }
    public static double roundToOneDecimal(double value) {
        DecimalFormat df = new DecimalFormat("#.#");
        return Float.parseFloat(df.format(value));
    }
    public static double calculateNewPrice(double originalPrice, float discount) {
        return originalPrice * (1 - discount / 100);
    }

}
