package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
        class MyCollector implements Collector<UserMeal, Map<LocalDate, List<UserMealWithExcess>>, List<UserMealWithExcess>> {
            private final Map<LocalDate, Integer> caloriesPerDays = new ConcurrentHashMap<>();

            @Override
            public Supplier<Map<LocalDate, List<UserMealWithExcess>>> supplier() {
                return ConcurrentHashMap::new;
            }

            @Override
            public BiConsumer<Map<LocalDate, List<UserMealWithExcess>>, UserMeal> accumulator() {
                return (map, element) -> {
                    LocalDateTime localDateTime = element.getDateTime();
                    caloriesPerDays.merge(localDateTime.toLocalDate(), element.getCalories(), Integer::sum);
                    if (TimeUtil.isBetweenHalfOpen(localDateTime.toLocalTime(), startTime, endTime)) {
                        List<UserMealWithExcess> userMealWithExcesses = map.getOrDefault(localDateTime.toLocalDate(), new ArrayList<>());
                        userMealWithExcesses.add(new UserMealWithExcess(localDateTime, element.getDescription(), element.getCalories(), false));
                        map.put(localDateTime.toLocalDate(), userMealWithExcesses);
                    }
                };
            }

            @Override
            public BinaryOperator<Map<LocalDate, List<UserMealWithExcess>>> combiner() {
                return (map1, map2) -> {
                    map2.forEach((key, value) -> map1.merge(key, value, (oldVal, newVal) -> {
                        oldVal.addAll(newVal);
                        return oldVal;
                    }));
                    return map1;
                };
            }

            @Override
            public Function<Map<LocalDate, List<UserMealWithExcess>>, List<UserMealWithExcess>> finisher() {
                return map -> {
                    List<UserMealWithExcess> result = new ArrayList<>();
                    for (LocalDate localDate : map.keySet()) {
                        if (caloriesPerDays.get(localDate) > caloriesPerDay)
                            map.get(localDate).forEach(um -> um.setExcess(true));
                        result.addAll(map.get(localDate));
                    }
                    return result;
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
        List<UserMealWithExcess> result = new ArrayList<>();
        class Util {
            final Map<LocalDate, Integer> caloriesPerDays = new HashMap<>();

            void sum(int i) {
                if (i == meals.size()) return;
                UserMeal userMeal = meals.get(i);
                caloriesPerDays.merge(userMeal.getDateTime().toLocalDate(), meals.get(i).getCalories(), Integer::sum);
                sum(++i);
                if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                    result.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(),
                            userMeal.getCalories(), caloriesPerDays.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay));
                }
            }
        }
        new Util().sum(0);
        return result;
    }


}
