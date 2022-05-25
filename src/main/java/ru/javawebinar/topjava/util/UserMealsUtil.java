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
        List<UserMealWithExcess> result = new ArrayList<>();
        Map<LocalDate, Integer> calories = new HashMap<>();
        for (UserMeal meal : meals) {
            calories.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
        }
        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(),
                        meal.getCalories(), calories.getOrDefault(meal.getDateTime().toLocalDate(), meal.getCalories()) > caloriesPerDay));
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
//        Map<LocalDate, Integer> calories = meals.stream().collect(
//                Collectors.groupingBy(p -> p.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));
        Map<LocalDate, Integer> calories = meals.stream().collect(
                Collectors.toMap(s -> s.getDateTime().toLocalDate(), UserMeal::getCalories, Integer::sum));
        return meals.stream().filter(x -> TimeUtil.isBetweenHalfOpen(x.getDateTime().toLocalTime(), startTime, endTime))
                .map(x -> new UserMealWithExcess(x.getDateTime(), x.getDescription(), x.getCalories(),
                        (calories.getOrDefault(x.getDateTime().toLocalDate(), 0) > caloriesPerDay))).collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByStreams2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream().collect(new MyCollector(startTime, endTime, caloriesPerDay));
    }

    public static List<UserMealWithExcess> filteredByCycles2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        //I'm thinking how to do this
        return null;
    }
}

    class MyCollector implements Collector<UserMeal, List<UserMeal>, List<UserMealWithExcess>> {
        private static Map<LocalDate, Integer> myMap = new HashMap<>();
        private static LocalTime start;
        private static LocalTime end;
        private static int caloriesPerDay;

        public MyCollector(LocalTime start, LocalTime end, int caloriesPerDay) {
            MyCollector.start = start;
            MyCollector.end = end;
            MyCollector.caloriesPerDay = caloriesPerDay;
        }

        @Override
        public Supplier<List<UserMeal>> supplier() {
            return ArrayList::new;
        }

        @Override
        public BiConsumer<List<UserMeal>, UserMeal> accumulator() {
            return (list, element) -> {
                myMap.merge(element.getDateTime().toLocalDate(), element.getCalories(), Integer::sum);
                if (TimeUtil.isBetweenHalfOpen(element.getDateTime().toLocalTime(), start, end)) {
                    list.add(element);
                }
            };
        }

        @Override
        public BinaryOperator<List<UserMeal>> combiner() {
            return (s1, s2) -> {
                s1.addAll(s2);
                return s1;
            };
        }

        @Override
        public Function<List<UserMeal>, List<UserMealWithExcess>> finisher() {
            return a -> a.stream().map(user -> new UserMealWithExcess(user.getDateTime(), user.getDescription(), user.getCalories(),
                    myMap.getOrDefault(user.getDateTime().toLocalDate(), 0) > caloriesPerDay)).collect(Collectors.toList());
        }

        @Override
        public Set<Characteristics> characteristics() {
            return EnumSet.of(Characteristics.CONCURRENT);
        }

    }


