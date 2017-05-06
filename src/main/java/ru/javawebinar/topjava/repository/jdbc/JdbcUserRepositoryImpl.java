package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final UserResultSetExtractor EXTRACTOR = new UserResultSetExtractor();

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            if (user.getRoles() != null) {
                List<Object[]> lst = new ArrayList<>(user.getRoles().size());
                user.getRoles().stream()
                        .map(Enum::toString)
                        .forEach(roleName -> lst.add(new Object[]{user.getId(), roleName}));
                jdbcTemplate.batchUpdate("insert into user_roles (user_id, role) values (?, ?)", lst);
            }
        } else {
            namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            " registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id",
                    parameterSource);
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("roles", user.getRoles().stream().map(Enum::toString).collect(Collectors.toList()));
            paramsMap.put("userId", user.getId());
            //удаление лишних
            namedParameterJdbcTemplate.update("DELETE FROM user_roles " +
                    " WHERE user_id = (:userId) AND role NOT IN (:roles)", paramsMap);
            List<String> rolesFromDb = jdbcTemplate.queryForList("SELECT role FROM user_roles " +
                    " WHERE user_id=? ", String.class, user.getId());
            //вставка недостающих
            List<Object[]> lst = new ArrayList<>();
            user.getRoles().stream()
                    .map(Enum::toString)
                    .filter(roleName -> !rolesFromDb.contains(roleName))
                    .forEach(roleName -> lst.add(new Object[]{user.getId(), roleName}));
            jdbcTemplate.batchUpdate("insert into user_roles (user_id, role) values (?, ?)", lst);
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users " +
                " LEFT JOIN user_roles ON user_roles.user_id=users.id" +
                " WHERE id=? ", EXTRACTOR, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users " +
                " LEFT JOIN user_roles ON user_roles.user_id=users.id " +
                " WHERE email=? ", EXTRACTOR, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users " +
                " LEFT JOIN user_roles ON user_roles.user_id=users.id " +
                " ORDER BY name, email", EXTRACTOR);
    }

    private static class UserResultSetExtractor implements ResultSetExtractor<List<User>> {
        public List<User> extractData(ResultSet rs) throws SQLException,
                DataAccessException {
            Map<Integer, User> usersMap = new HashMap<>();
            List<User> usersList = new ArrayList<>();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                User oldUser = usersMap.get(user.getId());
                if (oldUser != null) {
                    user = oldUser;
                } else {
                    usersMap.put(user.getId(), user);
                    usersList.add(user);
                    user.setName(rs.getString("name"));
                    user.setCaloriesPerDay(rs.getInt("calories_per_day"));
                    user.setEmail(rs.getString("email"));
                    user.setEnabled(rs.getBoolean("enabled"));
                    user.setRegistered(rs.getTimestamp("registered"));
                    user.setPassword(rs.getString("password"));
                }

                Set<Role> rolesSet = user.getRoles();
                if (rolesSet == null) {
                    rolesSet = new HashSet<>();
                    user.setRoles(rolesSet);
                }
                String roleString = rs.getString("role");
                if (roleString != null) {
                    rolesSet.add(Role.valueOf(roleString));
                }
            }
            return usersList;
        }
    }

}
