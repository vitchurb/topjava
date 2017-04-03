package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MealService {
    List<MealWithExceed> getByUser(Integer userId, int caloriesPerDay);

    List<MealWithExceed> getFilteredByUser(Integer userId,
                                           LocalDate dateFrom, LocalDate dateTo,
                                           LocalTime timeFrom, LocalTime timeTo,
                                           int caloriesPerDay);

    Meal get(Integer id, Integer userId) throws NotFoundException;

    void deleteById(int id, Integer userId) throws NotFoundException;

    void save(Meal meal, Integer userId) throws NotFoundException;

}