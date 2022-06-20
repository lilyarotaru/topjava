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
    public static final int USER_LUNCH_ID = START_SEQ + 4;
    public static final int USER_DINNER_ID = START_SEQ + 5;
    public static final int USER_PRE_MIDNIGHT_ID = START_SEQ + 6;
    public static final int USER_NIGHT_MEAL_ID = START_SEQ + 7;
    public static final int USER_BREAKFAST_2_ID = START_SEQ + 8;
    public static final int USER_LUNCH_2_ID = START_SEQ + 9;
    public static final int USER_DINNER_2_ID = START_SEQ + 10;
    public static final int ADMIN_BREAKFAST_ID = START_SEQ + 11;
    public static final int NOT_FOUND = 10;
    public static final LocalDate firstDay = LocalDate.of(2022, 6, 15);
    public static final LocalDate secondDay = LocalDate.of(2022, 6, 16);
    public static final LocalTime breakfast = LocalTime.of(10, 0);
    public static final LocalTime lunch = LocalTime.of(15, 0);
    public static final LocalTime dinner = LocalTime.of(18, 0);
    public static final LocalTime pre_midnight = LocalTime.of(23, 59);

    public static final Meal user_breakfast = new Meal(USER_BREAKFAST_ID, LocalDateTime.of(firstDay, breakfast), "Завтрак юзера", 600);
    public static final Meal user_lunch = new Meal(USER_LUNCH_ID, LocalDateTime.of(firstDay, lunch), "Обед юзера", 900);
    public static final Meal user_dinner = new Meal(USER_DINNER_ID, LocalDateTime.of(firstDay, dinner), "Ужин юзера", 500);
    public static final Meal user_pre_midnight = new Meal(USER_PRE_MIDNIGHT_ID, LocalDateTime.of(firstDay, pre_midnight), "Еда перед полуночью юзера", 150);
    public static final Meal user_night_meal = new Meal(USER_NIGHT_MEAL_ID, LocalDateTime.of(secondDay, LocalTime.MIDNIGHT), "Ночные перекусы юзера", 333);
    public static final Meal user_breakfast_2 = new Meal(USER_BREAKFAST_2_ID, LocalDateTime.of(secondDay, breakfast), "Завтрак юзера", 500);
    public static final Meal user_lunch_2 = new Meal(USER_LUNCH_2_ID, LocalDateTime.of(secondDay, LocalTime.NOON), "Ланч юзера", 850);
    public static final Meal user_dinner_2 = new Meal(USER_DINNER_2_ID, LocalDateTime.of(secondDay, dinner), "Ужин юзера", 600);
    public static final Meal admin_breakfast = new Meal(ADMIN_BREAKFAST_ID, LocalDateTime.of(firstDay, breakfast), "Завтрак админа", 400);

    public static Meal getNew() {
        return new Meal(LocalDateTime.parse("2020-02-02T00:00"), "New meal", 1234);
    }

    public static Meal getUpdated() {
        Meal updatedMeal = new Meal(user_breakfast);
        updatedMeal.setDateTime(LocalDateTime.parse("2000-01-01T07:00"));
        updatedMeal.setDescription("Updated meal");
        updatedMeal.setCalories(888);
        return updatedMeal;
    }

    public static Meal getNonExistent(){
        Meal nonExistent = getNew();
        nonExistent.setId(NOT_FOUND);
        return nonExistent;
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
