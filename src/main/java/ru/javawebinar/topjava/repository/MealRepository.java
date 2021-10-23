package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

public interface MealRepository {
    // null if updated meal do not belong to userId
    Meal save(Meal meal);

    // false if meal do not belong to userId
    boolean delete(int id, int UserId);

    // null if meal do not belong to userId
    Meal get(int id, int userId);

    // ORDERED dateTime desc
    Collection<Meal> getAll(int userId);

    Collection<Meal> getAll(LocalDate startDate, LocalDate endDate, int userId);
}
