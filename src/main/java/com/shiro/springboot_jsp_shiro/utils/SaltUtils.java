
package com.shiro.springboot_jsp_shiro.utils;

import java.util.Random;

public class SaltUtils {

    public static String getSalt(int n){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567890!@#$号^&*()".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            char c = chars[new Random().nextInt(chars.length)];
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }
}
