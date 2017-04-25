package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;

import static ru.javawebinar.topjava.UserTestData.USER_ID;

/**
 * Created by vit on 24.04.2017.
 */
@ActiveProfiles({Profiles.ACTIVE_DB, Profiles.DATAJPA})
public class UserServiceDataJpaTest extends UserServiceTest {

    @Test
    public void testGetWithMeals() throws Exception {
        User user = service.getWithMeals(USER_ID);
        user.getMeals().size();
    }
}
