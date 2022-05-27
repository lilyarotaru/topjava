package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDaySum = new HashMap<>();
        for (UserMeal meal : meals) {
            caloriesPerDaySum.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
        }
        List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(),
                        meal.getCalories(), caloriesPerDaySum.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
//        Map<LocalDate, Integer> caloriesPerDaySum = meals.stream().collect(
//                Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));
        Map<LocalDate, Integer> caloriesPerDaySum = meals.stream()
                .collect(Collectors.toMap(meal -> meal.getDateTime().toLocalDate(), UserMeal::getCalories, Integer::sum));
        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        (caloriesPerDaySum.get(meal.getDateTime().toLocalDate()) > caloriesPerDay)))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByStreams2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        class MyCollector implements Collector<UserMeal, List<UserMealWithExcess>, List<UserMealWithExcess>> {
            private final Map<LocalDate, Integer> caloriesPerDaySum = new HashMap<>();

            @Override
            public Supplier<List<UserMealWithExcess>> supplier() {
                return ArrayList::new;
            }

            @Override
            public BiConsumer<List<UserMealWithExcess>, UserMeal> accumulator() {
                return (list, element) -> {
                    caloriesPerDaySum.merge(element.getDateTime().toLocalDate(), element.getCalories(), Integer::sum);
                    if (TimeUtil.isBetweenHalfOpen(element.getDateTime().toLocalTime(), startTime, endTime)) {
                        list.add(new UserMealWithExcess(element.getDateTime(), element.getDescription(), element.getCalories(), false));
                    }
                };
            }

            @Override
            public BinaryOperator<List<UserMealWithExcess>> combiner() {
                return (list1, list2) -> {
                    list1.addAll(list2);
                    return list1;
                };
            }

            @Override
            public Function<List<UserMealWithExcess>, List<UserMealWithExcess>> finisher() {

                return list->{
                    List<LocalDate> daysOfExcessCalories = caloriesPerDaySum.keySet().stream()
                            .filter(key -> caloriesPerDaySum.get(key)>caloriesPerDay)
                            .collect(Collectors.toList());
                    list.stream()
                            .filter(um-> daysOfExcessCalories.contains(um.getDateTime().toLocalDate()))
                            .forEach(um-> um.setExcess(true));
                    return list;
                };
            }

            @Override
            public Set<Characteristics> characteristics() {
                return EnumSet.of(Characteristics.CONCURRENT);
            }

        }

        return meals.stream().collect(new MyCollector());

    }

    public static List<UserMealWithExcess> filteredByCycles2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        //I'm thinking how to do this
        return null;
    }
}
