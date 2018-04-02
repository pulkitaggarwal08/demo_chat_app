package com.demo_chat_app.pulkit.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pulkit on 15/2/18.
 */

public class StringUtils {

    private static Matcher matcher;
    private static Pattern VALID_EMAIL_ADDRESS_REGEX;

    public static boolean isEmpty(String data) {

        if (TextUtils.isEmpty(data)) {
            return true;
        }

        return false;
    }

    public static boolean isEmailMatch(String email) {

        VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);

        if (matcher.find()) {
            return true;
        }
        return false;
    }

}
