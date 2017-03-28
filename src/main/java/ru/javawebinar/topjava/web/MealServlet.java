package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDAO;
import ru.javawebinar.topjava.dao.MealDAOFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by vit on 26.03.2017.
 */
public class MealServlet extends HttpServlet {
    private static final Logger LOG = getLogger(MealServlet.class);
    private static final MealDAO mealDAO = MealDAOFactory.getMealDAO();

    private static final int MAX_CALORIES_PER_DAY = 1000;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("open meals page");
        if ("edit".equals(request.getParameter("action"))) {
            Long id = null;
            try {
                id = Long.parseLong(request.getParameter("id"));
            } catch (NumberFormatException e) {
            }
            if (request.getAttribute("meal") != null) {
                request.getRequestDispatcher("/meal/edit.jsp").forward(request, response);
            } else {
                Meal meal = id == null ? null : mealDAO.getById(id);
                if (meal != null) {
                    MealWithExceed mealWithExceed = MealsUtil.createWithExceed(meal, false);
                    mealWithExceed.setDateStr(TimeUtil.formatDate(meal.getDateTime()));
                    mealWithExceed.setTimeStr(TimeUtil.formatTime(meal.getDateTime()));
                    request.setAttribute("meal", mealWithExceed);
                }
                request.getRequestDispatcher("/meal/edit.jsp").forward(request, response);
            }
        } else if ("delete".equals(request.getParameter("action"))) {
            String mealIdToDelete = request.getParameter("id");
            Long mealIdToDeleteLong = null;
            try {
                mealIdToDeleteLong = Long.parseLong(mealIdToDelete);
            } catch (Exception e) {
            }
            mealDAO.delete(mealIdToDeleteLong);
            response.sendRedirect("meals");
        } else {
            List<Meal> meals = mealDAO.getAll();
            List<MealWithExceed> mealsWithExceeded = MealsUtil.getFilteredWithExceeded(
                    meals, LocalTime.MIN, LocalTime.MAX, MAX_CALORIES_PER_DAY);
            mealsWithExceeded = mealsWithExceeded.stream()
                    .sorted(Comparator.comparing(MealWithExceed::getDateTime))
                    .collect(Collectors.toList());
            mealsWithExceeded.forEach(
                    meal -> meal.setDateTimeStr(TimeUtil.formatDateTime(meal.getDateTime())));
            request.setAttribute("mealsWithExceeded", mealsWithExceeded);
            request.getRequestDispatcher("/meal/meals.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        LOG.debug("open meals page. Post.");
        if (request.getParameter("save") != null) {
            List<String> errors = new ArrayList<>();
            String id = request.getParameter("id");
            String dateStr = request.getParameter("date");
            String timeStr = request.getParameter("time");
            String description = request.getParameter("description");
            String calories = request.getParameter("calories");
            LocalDate localDate = null;
            LocalTime localTime = null;
            Integer caloriesInt = null;
            Long idLong = null;
            try {
                localDate = TimeUtil.parseDate(dateStr);
            } catch (DateTimeException e) {
            }
            try {
                localTime = TimeUtil.parseTime(timeStr);
            } catch (DateTimeException e) {
            }
            try {
                caloriesInt = Integer.parseUnsignedInt(calories);
            } catch (NumberFormatException e) {
            }
            try {
                idLong = Long.parseLong(id);
            } catch (NumberFormatException e) {
            }
            if (localDate == null) {
                errors.add("Формат даты неверный");
            }
            if (localTime == null) {
                errors.add("Формат времени неверный");
            }
            if (description == null || description.isEmpty()) {
                errors.add("Описание должно быть не пустым");
            }
            if (caloriesInt == null || caloriesInt <= 0) {
                errors.add("Необходимо указать калории");
            }
            if (errors.isEmpty()) {
                mealDAO.save(new Meal(idLong, LocalDateTime.of(localDate, localTime), description, caloriesInt));
                response.sendRedirect("meals");
            } else {
                request.setAttribute("errors", errors);
                MealWithExceed mealWithExceed = new MealWithExceed(idLong, dateStr, timeStr, description,
                        caloriesInt == null ? 0 : caloriesInt);
                request.setAttribute("meal", mealWithExceed);
                request.getRequestDispatcher("/meal/edit.jsp").forward(request, response);
            }
        } else {
            response.sendRedirect("meals");
        }
    }

}
