package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final int USER_BREAKFAST_ID = START_SEQ + 3;
    public static final int USER_DINNER_ID = START_SEQ + 4;
    public static final int USER_NIGHT_MEAL_ID = START_SEQ + 5;
    public static final int ADMIN_BREAKFAST_ID = START_SEQ + 6;
    public static final int NOT_FOUND = 10;
    public static final LocalDate firstDay = LocalDate.of(2022, 6, 15);
    public static final LocalTime breakfast = LocalTime.of(10, 0);
    public static final LocalTime dinner = LocalTime.of(15, 0);

    public static final Meal user_breakfast = new Meal(USER_BREAKFAST_ID, LocalDateTime.of(firstDay, breakfast), "Завтрак юзера", 600);
    public static final Meal user_dinner = new Meal(USER_DINNER_ID, LocalDateTime.of(firstDay, dinner), "Обед юзера", 1000);
    public static final Meal user_night_meal = new Meal(USER_NIGHT_MEAL_ID, LocalDateTime.of(firstDay.plusDays(1), LocalTime.MIDNIGHT), "Ночные перекусы юзера", 333);
    public static final Meal admin_breakfast = new Meal(ADMIN_BREAKFAST_ID, LocalDateTime.of(firstDay, breakfast), "Завтрак админа", 400);

    public static Meal getNew() {
        return new Meal(LocalDateTime.of(LocalDate.now(), LocalTime.NOON), "New meal", 1234);
    }

    public static Meal getUpdated() {
        Meal updatedMeal = new Meal(user_breakfast);
        updatedMeal.setDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.NOON));
        updatedMeal.setDescription("Updated meal");
        updatedMeal.setCalories(888);
        return updatedMeal;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
