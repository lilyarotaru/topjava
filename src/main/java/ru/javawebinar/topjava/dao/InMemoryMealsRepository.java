package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealsRepository implements CrudRepository<Meal> {
    private final AtomicInteger counter;
    private final Map<Integer, Meal> meals;

    public InMemoryMealsRepository() {
        this.counter = new AtomicInteger(1);
        this.meals = new ConcurrentHashMap<>();
        add(new Meal(LocalDateTime.of(2022, Month.MARCH, 29, 10, 0), "Завтрак", 490));
        add(new Meal(LocalDateTime.of(2022, Month.MARCH, 29, 13, 0), "Обед", 950));
        add(new Meal(LocalDateTime.of(2022, Month.MARCH, 29, 20, 0), "Ужин", 530));
        add(new Meal(LocalDateTime.of(2022, Month.MARCH, 30, 0, 0), "Еда на граничное значение", 100));
        add(new Meal(LocalDateTime.of(2022, Month.MARCH, 30, 10, 0), "Завтрак", 430));
        add(new Meal(LocalDateTime.of(2022, Month.MARCH, 30, 13, 0), "Обед", 900));
        add(new Meal(LocalDateTime.of(2022, Month.MARCH, 30, 20, 0), "Ужин", 600));
    }   //also may initialize it in non-static block;

    @Override
    public Meal add(Meal meal) {
        meal.setId(counter.getAndIncrement());
        meals.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public Meal update(Meal meal) {
        return (meals.replace(meal.getId(), meal) == null ? null : meal);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal getById(int id) {
        return meals.get(id);
    }

    @Override
    public void delete(int id) {
        meals.remove(id);
    }
}
