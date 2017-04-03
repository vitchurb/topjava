package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by vit on 02.04.2017.
 */
public class UsersUtil {
    public static final List<User> USERS = Arrays.asList(
            new User(null, "Admin", "admin@company.com", "12345",
                    1700, true, EnumSet.of(Role.ROLE_ADMIN)),
            new User(null, "User1", "user1@company.com", "12345",
                    2700, true, EnumSet.of(Role.ROLE_USER)),
            new User(null, "User2", "user2@company.com", "12345",
                    100, true, EnumSet.of(Role.ROLE_USER)));
}
