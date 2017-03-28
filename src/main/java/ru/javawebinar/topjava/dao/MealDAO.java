package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

/**
 * Created by vit on 26.03.2017.
 */
public interface MealDAO {
    void save(Meal meal);

    Meal getById(Long id);

    void delete(Long id);

    List<Meal> getAll();

}
