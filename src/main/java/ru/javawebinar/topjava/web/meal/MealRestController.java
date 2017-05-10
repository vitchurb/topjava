package ru.javawebinar.topjava.web.meal;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
@RequestMapping(value = "/api/meals")
public class MealRestController {
    private static final Logger LOG = LoggerFactory.getLogger(MealRestController.class);

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    @GetMapping(value = "/{id}/get")
    @ResponseBody
    public String get(@RequestParam(name = "id") int id) {
        int userId = AuthorizedUser.id();
        LOG.info("get meal {} for User {} by api", id, userId);
        String result = createSuccessAnswer(service.get(id, userId));
        LOG.debug("Return 200: " + result);
        return result;
    }

    @GetMapping(value = "/{id}/delete")
    @ResponseBody
    public String delete(@RequestParam(name = "id") int id) {
        int userId = AuthorizedUser.id();
        LOG.info("delete meal {} for User {} by api", id, userId);
        service.delete(id, userId);
        String result = createSuccessAnswer(null);
        LOG.debug("Return 200: " + result);
        return result;
    }

    @GetMapping(value = "/getAll")
    @ResponseBody
    public String getAll() {
        int userId = AuthorizedUser.id();
        LOG.info("getAll for User {} by api", userId);
        List<MealWithExceed> lstMeals = MealsUtil.getWithExceeded(service.getAll(userId), AuthorizedUser.getCaloriesPerDay());
        String result = createSuccessAnswerMeals(lstMeals);
        LOG.debug("Return 200: " + result);
        return result;
    }

    public Meal create(Meal meal) {
        int userId = AuthorizedUser.id();
        checkNew(meal);
        LOG.info("create {} for User {}", meal, userId);
        return service.save(meal, userId);
    }

    public void update(Meal meal, int id) {
        int userId = AuthorizedUser.id();
        checkIdConsistent(meal, id);
        LOG.info("update {} for User {}", meal, userId);
        service.update(meal, userId);
    }

    @GetMapping(value = "/getBetween")
    @ResponseBody
    public String getBetween(@RequestParam(required = false) LocalDate startDate,
                             @RequestParam(required = false) LocalTime startTime,
                             @RequestParam(required = false) LocalDate endDate,
                             @RequestParam(required = false) LocalTime endTime) {
        int userId = AuthorizedUser.id();
        LOG.info("getBetween dates({} - {}) time({} - {}) for User {} by api", startDate, endDate, startTime, endTime, userId);
        List<MealWithExceed> lstMeals = MealsUtil.getFilteredWithExceeded(
                service.getBetweenDates(
                        startDate != null ? startDate : DateTimeUtil.MIN_DATE,
                        endDate != null ? endDate : DateTimeUtil.MAX_DATE, userId),
                startTime != null ? startTime : LocalTime.MIN,
                endTime != null ? endTime : LocalTime.MAX,
                AuthorizedUser.getCaloriesPerDay()
        );
        String result = createSuccessAnswerMeals(lstMeals);
        LOG.debug("Return 200: " + result);
        return result;
    }

    private String createSuccessAnswer(Meal meal) {
        JSONObject objJSON = new JSONObject();
        objJSON.put("result", "success");
        objJSON.put("errorCode", 0);
        if (meal != null) {
            JSONObject objMeal = new JSONObject();
            objMeal.put("dateTime", meal.getDateTime().toString());
            objMeal.put("description", meal.getDescription());
            objMeal.put("calories", meal.getCalories());
            objJSON.put("meal", objMeal);
        }
        return objJSON.toJSONString();
    }

    private String createSuccessAnswerMeals(List<MealWithExceed> mealWithExceeds) {
        JSONObject objJSON = new JSONObject();
        objJSON.put("result", "success");
        objJSON.put("errorCode", 0);
        JSONArray arrMeals = new JSONArray();
        for (MealWithExceed meal : mealWithExceeds) {
            JSONObject objMeal = new JSONObject();
            objMeal.put("id", meal.getId());
            objMeal.put("dateTime", meal.getDateTime().toString());
            objMeal.put("description", meal.getDescription());
            objMeal.put("calories", meal.getCalories());
            objMeal.put("exceed", meal.isExceed());
            arrMeals.add(objMeal);
        }
        objJSON.put("meals", arrMeals);
        return objJSON.toJSONString();
    }

}