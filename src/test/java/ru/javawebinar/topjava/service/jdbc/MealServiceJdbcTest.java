package ru.javawebinar.topjava.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.MealServiceTest;

/**
 * Created by vit on 24.04.2017.
 */
@ActiveProfiles(Profiles.JDBC)
public class MealServiceJdbcTest extends MealServiceTest {
}
