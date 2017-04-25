package ru.javawebinar.topjava.repository.jdbc.hsqldb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.repository.jdbc.JdbcMealRepositoryImpl;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Repository
@Profile(Profiles.HSQL_DB)
public class JdbcMealRepositoryHsqlImpl extends JdbcMealRepositoryImpl {

    @Autowired
    public JdbcMealRepositoryHsqlImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(dataSource, jdbcTemplate, namedParameterJdbcTemplate);
    }

    protected Timestamp toSqlDatetime(LocalDateTime ldt) {
        return ldt == null ? null : Timestamp.valueOf(ldt);
    }

}
