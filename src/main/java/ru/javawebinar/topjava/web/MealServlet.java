package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * User: gkislin
 * Date: 19.08.2014
 */
public class MealServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(MealServlet.class);

    private ConfigurableApplicationContext appCtx;
    private MealRestController mealRestController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealRestController = appCtx.getBean(MealRestController.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        String action = request.getParameter("action");
        if ("changeUser".equals(action)) {
            AuthorizedUser.setId(Integer.valueOf(request.getParameter("userSelectId")));
            request.getSession().setAttribute("userSelectId", request.getParameter("userSelectId"));
            response.sendRedirect("meals");
        } else if ("search".equals(action)) {
            request.getSession().setAttribute("filterDateFrom", parse(() -> DateTimeUtil.parseDate(request.getParameter("filterDateFrom"))));
            request.getSession().setAttribute("filterDateTo", parse(() -> DateTimeUtil.parseDate(request.getParameter("filterDateTo"))));
            request.getSession().setAttribute("filterTimeFrom", parse(() -> DateTimeUtil.parseTime(request.getParameter("filterTimeFrom"))));
            request.getSession().setAttribute("filterTimeTo", parse(() -> DateTimeUtil.parseTime(request.getParameter("filterTimeTo"))));
            response.sendRedirect("meals");
        } else {
            Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                    null,
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.valueOf(request.getParameter("calories")));

            LOG.info(meal.isNew() ? "Create {}" : "Update {}", meal);
            mealRestController.save(meal); //EXCEPTION
            response.sendRedirect("meals");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                LOG.info("Delete {}", id);
                mealRestController.deleteById(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = action.equals("create") ?
                        new Meal(null, null, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        mealRestController.getById(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/meal.jsp").forward(request, response);
                break;
            case "all":
            default:
                LOG.info("getAll");
                request.setAttribute("meals", mealRestController.getByCurrentUser(
                        (LocalDate) request.getSession().getAttribute("filterDateFrom"),
                        (LocalDate) request.getSession().getAttribute("filterDateTo"),
                        (LocalTime) request.getSession().getAttribute("filterTimeFrom"),
                        (LocalTime) request.getSession().getAttribute("filterTimeTo"))
                );
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }

    static <T> T parse(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        appCtx.close();
    }
}