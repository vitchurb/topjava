package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;


/**
 * Created by vit on 11.04.2017.
 */
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class MealServiceTest {
    @Autowired
    private MealService service;

    @Autowired
    private DbPopulator dbPopulator;

    @Before
    public void setUp() throws Exception {
        dbPopulator.execute();
    }

    @Test
    public void testGet() throws Exception {
        Meal meal = service.get(MEAL_USER_ID_1, USER_ID);
        MATCHER.assertEquals(MEAL_USER_1, meal);
        meal = service.get(MEAL_ADMIN_ID_1, ADMIN_ID);
        MATCHER.assertEquals(MEAL_ADMIN_1, meal);
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundGet() throws Exception {
        service.get(MEAL_USER_ID_1, ADMIN_ID);
    }

    @Test
    public void testDelete() throws Exception {
        service.delete(MEAL_USER_ID_1, USER_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(MEAL_USER_3, MEAL_USER_2), service.getAll(USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundDelete() throws Exception {
        service.delete(MEAL_USER_ID_1, ADMIN_ID);
    }

    @Test
    public void getBetweenDates() throws Exception {
        Collection<Meal> all = service.getBetweenDates(LocalDate.parse("2017-01-11")
                , LocalDate.parse("2017-01-11"), USER_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(MEAL_USER_3, MEAL_USER_2), all);
    }

    @Test
    public void getBetweenDateTimes() throws Exception {
        Collection<Meal> all = service.getBetweenDateTimes(
                LocalDateTime.parse("2017-01-11T17:02:00"),
                LocalDateTime.parse("2017-01-11T17:33:00"), USER_ID);
        MATCHER.assertCollectionEquals(Collections.singleton(MEAL_USER_2), all);
    }

    @Test
    public void testGetAll() throws Exception {
        Collection<Meal> all = service.getAll(ADMIN_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(MEAL_ADMIN_2, MEAL_ADMIN_1), all);
    }

    @Test
    public void update() throws Exception {
        Meal updated = new Meal(MEAL_USER_1);
        updated.setDescription("Updated Завтрак");
        updated.setCalories(588);
        service.update(updated, USER_ID);
        MATCHER.assertEquals(updated, service.get(MEAL_USER_ID_1, USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundUpdate() throws Exception {
        Meal updated = new Meal(MEAL_USER_1);
        updated.setDescription("Updated Завтрак");
        updated.setCalories(588);
        service.update(updated, ADMIN_ID);
    }


    @Test
    public void testSave() throws Exception {
        Meal newMeal = new Meal(null,
                LocalDateTime.parse("2017-01-11T10:03:00"), "завтрак", 775);
        Meal created = service.save(newMeal, USER_ID);
        newMeal.setId(created.getId());
        MATCHER.assertCollectionEquals(Arrays.asList(MEAL_USER_3, MEAL_USER_2, newMeal, MEAL_USER_1), service.getAll(USER_ID));
    }

}