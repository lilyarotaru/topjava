package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.NOT_FOUND;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest {

    @Test
    public void getWithUser() {
        Meal actual = service.getWithUser(MEAL1_ID, USER_ID);
        Meal expected = new Meal(meal1);
        MEAL_MATCHER.assertMatch(actual, expected);
        USER_MATCHER.assertMatch(actual.getUser(), user);
    }

    @Test
    public void getWithUserNotFound() {
        assertThrows(NotFoundException.class, () -> service.getWithUser(NOT_FOUND, USER_ID));
    }
}
