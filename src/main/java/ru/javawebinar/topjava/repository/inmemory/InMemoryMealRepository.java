package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.stream().limit(7).forEach(m -> this.save(m, 1));
        MealsUtil.meals.stream().skip(7).forEach(m -> this.save(m, 2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {}", meal);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        }
        //  meal.getUserId() != userId      always true, because we don't have field userId in MealForm
        //  if we change mealForm.jsp and add hidden userId field to attribute Meal, we can check this way, but it unsafely ?
        if (repository.get(meal.getId()).getUserId() != userId) return null;
        meal.setUserId(userId);
        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {}", id);
        Meal meal = repository.get(id);
        if (meal != null && meal.getUserId() != userId) return false;
        return repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {}", id);
        Meal meal = repository.get(id);
        if (meal != null && meal.getUserId() != userId) return null;
        return meal;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        log.info("getAll");
        return repository.values().stream()
                .filter(m -> m.getUserId() == userId)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                //.sorted(Comparator.comparing(Meal::getDateTime, Comparator.reverseOrder()))
                //.sorted((m1, m2)-> m2.getDateTime().compareTo(m1.getDateTime()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getAllFilter(int userId, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("getAllFilter");
        return getAll(userId).stream()
                .filter(m -> DateTimeUtil.<LocalDateTime>isBetweenHalfOpen(m.getDateTime(), startDate, endDate))
                .collect(Collectors.toList());
    }
}

