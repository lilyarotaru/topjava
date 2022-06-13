package ru.javawebinar.topjava.web;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {
    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;
    private static int authUserId = 1;

    public static int authUserId() {
        return authUserId;
    }

    public static void setAuthUserId(int authUserId) {
        SecurityUtil.authUserId = authUserId == USER_ID ? USER_ID : ADMIN_ID;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}