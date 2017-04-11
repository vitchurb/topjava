package ru.javawebinar.topjava;

import ru.javawebinar.topjava.matcher.ModelMatcher;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;

import static ru.javawebinar.topjava.model.BaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int MEAL_USER_ID_1 = START_SEQ + 2;
    public static final int MEAL_USER_ID_2 = START_SEQ + 3;
    public static final int MEAL_USER_ID_3 = START_SEQ + 4;
    public static final int MEAL_ADMIN_ID_1 = START_SEQ + 5;
    public static final int MEAL_ADMIN_ID_2 = START_SEQ + 6;

    public static final Meal MEAL_USER_1 = new Meal(MEAL_USER_ID_1,
            LocalDateTime.parse("2017-01-10T13:42:00"), "Завтрак", 515);
    public static final Meal MEAL_USER_2 = new Meal(MEAL_USER_ID_2,
            LocalDateTime.parse("2017-01-11T17:02:00"), "Обед", 1054);
    public static final Meal MEAL_USER_3 = new Meal(MEAL_USER_ID_3,
            LocalDateTime.parse("2017-01-11T21:58:00"), "Ужин", 101);
    public static final Meal MEAL_ADMIN_1 = new Meal(MEAL_ADMIN_ID_1,
            LocalDateTime.parse("2017-01-10T10:03:00"), "Завтрак", 500);
    public static final Meal MEAL_ADMIN_2 = new Meal(MEAL_ADMIN_ID_2,
            LocalDateTime.parse("2017-01-11T11:00:00"), "Завтрак", 700);

    public static final ModelMatcher<Meal> MATCHER = new ModelMatcher();
}
