package ru.javawebinar.topjava.service.datajpa;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.MealTestData.meals;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest {

    @Test
    public void getWithMeals() {
        User actual = service.getWithMeals(USER_ID);
        User expected = new User(user);
        USER_MATCHER.assertMatch(actual, expected);
        MEAL_MATCHER.assertMatch(actual.getMeals(), meals);
    }

    @Test
    public void getWithMealsNotFound() {
        assertThrows(NotFoundException.class, () -> service.getWithMeals(NOT_FOUND));
    }

    @Test
    public void getWithEmptyMeals(){
        User actual = service.getWithMeals(GUEST_ID);
        User expected = new User(guest);
        USER_MATCHER.assertMatch(actual, expected);
        Assert.assertTrue(actual.getMeals().isEmpty());
//        Assert.assertEquals(Collections.emptyList(), actual.getMeals()); //just compare like an object or string
//        Assertions.assertIterableEquals(Collections.emptyList(), actual.getMeals()); //only in JUnit5
    }
}
