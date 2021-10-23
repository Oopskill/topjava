package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);


    {
        MealsUtil.meals.forEach(s -> this.save(s));
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }

        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);

        // handle case: update, but not present in storage
    }

    @Override
    public boolean delete(int id, int userId) {
        if (repository.get(id).getUserId() == userId) {
            return repository.remove(id) != null;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        if (repository.get(id).getUserId() == userId) {
            return repository.get(id);
        }
        return null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        if (userId == 1){
            return new ArrayList<>(repository.values());
        }
        return repository.values().stream()
                .filter(m -> m.getUserId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAll(LocalDate startDate, LocalDate endDate, int userId) {
        return getAll(userId).stream()
                .filter(m -> DateTimeUtil.isBetweenHalfOpenDate(m.getDate(), startDate, endDate))
                .sorted(Comparator.comparing(Meal::getDate).reversed())
                .collect(Collectors.toList());
    }
}