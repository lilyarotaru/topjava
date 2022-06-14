package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MealsUtil {
    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public static final List<Meal> userMeals = Arrays.asList(
            new Meal(LocalDateTime.of(2022, Month.JANUARY, 30, 10, 0), "Завтрак юзера", 520),
            new Meal(LocalDateTime.of(2022, Month.JANUARY, 30, 13, 0), "Обед юзера", 840),
            new Meal(LocalDateTime.of(2022, Month.JANUARY, 30, 20, 0), "Ужин юзера", 630),
            new Meal(LocalDateTime.of(2022, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(LocalDateTime.of(2022, Month.JANUARY, 31, 10, 0), "Завтрак юзера", 1000),
            new Meal(LocalDateTime.of(2022, Month.JANUARY, 31, 13, 0), "Бизнес-ланч юзера", 530),
            new Meal(LocalDateTime.of(2022, Month.JANUARY, 31, 20, 0), "Ужин юзера", 410)
    );

    public static final List<Meal> adminMeals = Arrays.asList(
            new Meal(LocalDateTime.of(2022, Month.MAY, 20, 10, 0), "Breakfast admin", 470),
            new Meal(LocalDateTime.of(2022, Month.MAY, 20, 13, 0), "Lunch admin", 850),
            new Meal(LocalDateTime.of(2022, Month.MAY, 20, 18, 0), "Dinner admin", 1000),
            new Meal(LocalDateTime.of(2022, Month.MAY, 20, 23, 59), "Food before midnight admin ", 130),
            new Meal(LocalDateTime.of(2022, Month.MAY, 21, 0, 0), "Meal for border admin ", 100),
            new Meal(LocalDateTime.of(2022, Month.MAY, 21, 10, 0), "Brunch admin", 750),
            new Meal(LocalDateTime.of(2022, Month.MAY, 21, 13, 0), "Lunch admin", 600),
            new Meal(LocalDateTime.of(2022, Month.MAY, 21, 20, 0), "Ужин admin", 420)
    );

    public static List<MealTo> getTos(Collection<Meal> meals, int caloriesPerDay) {
        return filterByPredicate(meals, caloriesPerDay, meal -> true);
    }

    public static List<MealTo> getFilteredTos(Collection<Meal> meals, int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        return filterByPredicate(meals, caloriesPerDay, meal -> DateTimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime));
    }

    private static List<MealTo> filterByPredicate(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(filter)
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
