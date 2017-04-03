package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class MealServiceImpl implements MealService {

    @Autowired
    private MealRepository mealRepository;

    public List<MealWithExceed> getByUser(Integer userId, int caloriesPerDay) {
        return MealsUtil.getWithExceeded(
                mealRepository.getAll(userId), caloriesPerDay);
    }

    public List<MealWithExceed> getFilteredByUser(Integer userId,
                                                  LocalDate dateFrom, LocalDate dateTo,
                                                  LocalTime timeFrom, LocalTime timeTo,
                                                  int caloriesPerDay) {
        return MealsUtil.getFilteredWithExceeded(
                mealRepository.getAllFilteredByDate(userId, dateFrom, dateTo),
                timeFrom, timeTo, caloriesPerDay);
    }

    public Meal get(Integer id, Integer userId) throws NotFoundException {
        return ValidationUtil.checkNotFoundWithId(mealRepository.get(id, userId), id);
    }

    public void deleteById(int id, Integer userId) throws NotFoundException {
        ValidationUtil.checkNotFoundWithId(mealRepository.delete(id, userId), id);
    }

    public void save(Meal meal, Integer userId) throws NotFoundException {
        meal.setUserId(userId);
        if (meal.getId() == null)
            ValidationUtil.checkNotFound(mealRepository.save(meal), "null");
        else
            ValidationUtil.checkNotFoundWithId(mealRepository.save(meal), meal.getId());
    }

}