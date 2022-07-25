package ru.javawebinar.topjava;

import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;

public class MealToTestData {

    public static final MatcherFactory.Matcher<MealTo> MEAL_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MealTo.class);

    public static final MealTo mealTo1 = makeMealTo(meal1, false);
    public static final MealTo mealTo2 = makeMealTo(meal2, false);
    public static final MealTo mealTo3 = makeMealTo(meal3, false);
    public static final MealTo mealTo4 = makeMealTo(meal4, true);
    public static final MealTo mealTo5 = makeMealTo(meal5, true);
    public static final MealTo mealTo6 = makeMealTo(meal6, true);
    public static final MealTo mealTo7 = makeMealTo(meal7, true);

    public static final List<MealTo> mealsTo = List.of(mealTo7, mealTo6, mealTo5, mealTo4, mealTo3, mealTo2, mealTo1);

    public static List<MealTo> readMealToFromJson(ResultActions action) throws UnsupportedEncodingException {
        return JsonUtil.readValues(action.andReturn().getResponse().getContentAsString(), MealTo.class);
    }

    private static MealTo makeMealTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}