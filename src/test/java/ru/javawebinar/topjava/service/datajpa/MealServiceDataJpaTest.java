package ru.javawebinar.topjava.service.datajpa;

import org.hibernate.Hibernate;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealServiceTest;

import static ru.javawebinar.topjava.MealTestData.MEAL1_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

/**
 * Created by vit on 24.04.2017.
 */
@ActiveProfiles(Profiles.DATAJPA)
public class MealServiceDataJpaTest extends MealServiceTest {

    @Test
    public void testGetWithUser() throws Exception {
        Meal meal = service.getWithUser(MEAL1_ID, USER_ID);
        Assert.assertTrue(Hibernate.isInitialized(meal.getUser()));
    }

}
