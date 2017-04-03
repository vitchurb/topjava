package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController {
    @Autowired
    private MealService mealService;

    public List<MealWithExceed> getByCurrentUser(LocalDate dateFrom, LocalDate dateTo,
                                                 LocalTime timeFrom, LocalTime timeTo) {
        return mealService.getFilteredByUser(AuthorizedUser.id(),
                dateFrom, dateTo, timeFrom, timeTo,
                AuthorizedUser.getCaloriesPerDay());
    }

    public Meal getById(Integer id) throws NotFoundException {
        return mealService.get(id, AuthorizedUser.id());
    }

    public void deleteById(Integer id) throws NotFoundException {
        mealService.deleteById(id, AuthorizedUser.id());
    }

    public void save(Meal meal) throws NotFoundException {
        mealService.save(meal, AuthorizedUser.id());

    }

}