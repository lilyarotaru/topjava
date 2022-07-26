package ru.javawebinar.topjava;

import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;

public class MealToTestData {

    public static final MatcherFactory.Matcher<MealTo> MEAL_TO_MATCHER = MatcherFactory.usingEquals(MealTo.class);

    public static final MealTo mealTo1 = MealsUtil.createTo(meal1, false);
    public static final MealTo mealTo2 = MealsUtil.createTo(meal2, false);
    public static final MealTo mealTo3 = MealsUtil.createTo(meal3, false);
    public static final MealTo mealTo4 = MealsUtil.createTo(meal4, true);
    public static final MealTo mealTo5 = MealsUtil.createTo(meal5, true);
    public static final MealTo mealTo6 = MealsUtil.createTo(meal6, true);
    public static final MealTo mealTo7 = MealsUtil.createTo(meal7, true);

    public static final List<MealTo> mealsTo = List.of(mealTo7, mealTo6, mealTo5, mealTo4, mealTo3, mealTo2, mealTo1);
}