package ru.javawebinar.topjava.DAO;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryRepository implements CRUDRepo<Meal, MealTo> {

    private static final AtomicInteger count = new AtomicInteger(1);
    private static final int caloriesPerDay = 2000;
    private static final Map<Integer, Meal> mealsMap;
    private static final List<Meal> meals;

    static {
        meals = new CopyOnWriteArrayList<>(Arrays.asList(
                new Meal(LocalDateTime.of(2022, Month.MARCH, 29, 10, 0), "Завтрак", 490),
                new Meal(LocalDateTime.of(2022, Month.MARCH, 29, 13, 0), "Обед", 950),
                new Meal(LocalDateTime.of(2022, Month.MARCH, 29, 20, 0), "Ужин", 530),
                new Meal(LocalDateTime.of(2022, Month.MARCH, 30, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2022, Month.MARCH, 30, 10, 0), "Завтрак", 430),
                new Meal(LocalDateTime.of(2022, Month.MARCH, 30, 13, 0), "Обед", 900),
                new Meal(LocalDateTime.of(2022, Month.MARCH, 30, 20, 0), "Ужин", 600)
        ));
        mealsMap = new ConcurrentHashMap<>();
        meals.stream().peek(meal -> meal.setId(count.getAndIncrement()))
                .forEach(meal -> mealsMap.put(meal.getId(), meal));
    }

    @Override
    public void add(Meal meal) {
        meal.setId(count.getAndIncrement());
        meals.add(meal);
        mealsMap.put(meal.getId(), meal);
    }

    @Override
    public void update(int id, Meal meal) {
        meal.setId(id);
        meals.remove(mealsMap.get(id));     //delete from List old version of Meal
        meals.add(meal);                    //add to List updated Meal
        mealsMap.replace(id, meal);        //refresh Map
    }

    @Override
    public List<MealTo> getAll() {
        return MealsUtil.filteredByStreams(meals,
                LocalTime.of(0, 0), LocalTime.of(23, 59, 59), caloriesPerDay);
    }

    @Override
    public Meal getById(int id) {
        return mealsMap.get(id);
    }

    @Override
    public void delete(int id) {
        meals.remove(mealsMap.get(id));
        mealsMap.remove(id);
    }
}
