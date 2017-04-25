package ru.javawebinar.topjava.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.UserServiceTest;

/**
 * Created by vit on 24.04.2017.
 */
@ActiveProfiles({Profiles.ACTIVE_DB, Profiles.JDBC})
public class UserServiceJdbcTest extends UserServiceTest {
}
