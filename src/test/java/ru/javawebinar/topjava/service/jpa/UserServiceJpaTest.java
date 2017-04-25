package ru.javawebinar.topjava.service.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.UserServiceTest;

/**
 * Created by vit on 24.04.2017.
 */
@ActiveProfiles(Profiles.JPA)
public class UserServiceJpaTest extends UserServiceTest {
}
