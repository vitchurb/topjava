package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.filters.MealFilter;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static ru.javawebinar.topjava.util.ValidationUtil.checkIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
@RequestMapping(value = "/meals")
@SessionAttributes("mealFilter")
public class MealWebController {
    private static final Logger LOG = LoggerFactory.getLogger(MealRestController.class);

    private final MealService service;

    @Autowired
    public MealWebController(MealService service) {
        this.service = service;
    }

    @ModelAttribute("mealFilter")
    public MealFilter getMealFilter() {
        return new MealFilter();
    }

    @GetMapping(value = "")
    public String list(Model model, MealFilter mealFilter) {
        int userId = AuthorizedUser.id();
        LOG.info("getBetween dates({} - {}) time({} - {}) for User {}",
                mealFilter.getStartDate(), mealFilter.getEndDate(),
                mealFilter.getStartTime(), mealFilter.getEndTime(), userId);
        model.addAttribute("meals",
                MealsUtil.getFilteredWithExceeded(
                        service.getBetweenDates(
                                mealFilter.getStartDate() != null ? mealFilter.getStartDate() : DateTimeUtil.MIN_DATE,
                                mealFilter.getEndDate() != null ? mealFilter.getEndDate() : DateTimeUtil.MAX_DATE, userId),
                        mealFilter.getStartTime() != null ? mealFilter.getStartTime() : LocalTime.MIN,
                        mealFilter.getEndTime() != null ? mealFilter.getEndTime() : LocalTime.MAX,
                        AuthorizedUser.getCaloriesPerDay()
                ));
        return "/meals";
    }

    @GetMapping(value = "/{id}/update")
    public String update(@PathVariable Integer id, Model model) {
        int userId = AuthorizedUser.id();
        LOG.info("get meal {} for User {}", id, userId);
        model.addAttribute("meal", service.get(id, userId));
        return "meal";
    }

    @GetMapping(value = "/create")
    public String create(Model model) {
        int userId = AuthorizedUser.id();
        LOG.info("create meal for User {}", userId);
        model.addAttribute("meal", new Meal(LocalDateTime.now().
                truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "meal";
    }

    @GetMapping(value = "/{id}/delete")
    public String delete(@PathVariable Integer id) {
        int userId = AuthorizedUser.id();
        LOG.info("delete meal {} for User {}", id, userId);
        service.delete(id, userId);
        return "redirect:/meals";
    }

    @PostMapping(value = {"/{id}/update", "/create"},
            params = "cancel")
    public String cancel() {
        return "redirect:/meals";
    }

    @PostMapping(value = {"/{id}/update", "/create"},
            params = "save")
    public String save(@ModelAttribute Meal meal,
                       @PathVariable(required = false) Integer id) {
        int userId = AuthorizedUser.id();
        LOG.info("save meal {} for User {}", id, userId);
        if (id == null) {
            checkNew(meal);
        } else {
            checkIdConsistent(meal, id);
        }
        service.save(meal, userId);
        return "redirect:/meals";
    }

    @PostMapping(value = "", params = "filter")
    public String filter(MealFilter mealFilter) {
        return "redirect:/meals";
    }
}