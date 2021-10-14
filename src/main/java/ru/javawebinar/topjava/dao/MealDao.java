package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MealDao {
    private static MealsUtil mealsUtil = new MealsUtil();
    private static List<Meal> meals = new ArrayList<>(mealsUtil.getAllMeals());
    private static List<MealTo> mealTos = new ArrayList<>(MealsUtil.getMealsTo(meals));

    public List<MealTo> getAllMeals() {
        return mealTos;
    }

    public void addMeal(LocalDateTime time, String description, int calories){
        Meal meal = new Meal(time, description, calories);
        meals.add(meal);
    }

    public void updateMeal(int id, Meal updMeal){
        Meal meal = meals.stream().filter(m -> m.getId().get() == id).findFirst().get();
        meal.setDescription(updMeal.getDescription());
        meal.setDateTime(updMeal.getDateTime());
        meal.setCalories(updMeal.getCalories());
    }

    public void deleteMeal(int id){
        meals.removeIf(m -> m.getId().get() == id);
        mealTos.removeIf(m -> m.getId().get() == id);
    }

    public MealTo getMeal(int id){
        return mealTos.stream().filter(m -> m.getId().get() == id).findFirst().get();
    }
}
