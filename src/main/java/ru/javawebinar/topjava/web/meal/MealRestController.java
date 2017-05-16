package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.annotation.CustomDateTime;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping(MealRestController.REST_URL)
public class MealRestController extends AbstractMealController {
    static final String REST_URL = "/rest/meals";
    private final Logger LOG = LoggerFactory.getLogger(getClass());


    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") int id) {
        super.delete(id);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Meal get(@PathVariable("id") int id) {
        return super.get(id);
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealWithExceed> getAll() {
        return super.getAll();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody Meal meal, @PathVariable("id") int id) {
        super.update(meal, id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createWithLocation(@RequestBody Meal meal) {
        Meal created = super.create(meal);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping(value = "/by", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealWithExceed> getBetween(@RequestParam(value = "startDateTime", required = false) @CustomDateTime
                                                   LocalDateTime startDateTime,
                                           @RequestParam(value = "endDateTime", required = false) @CustomDateTime
                                                   LocalDateTime endDateTime
    ) {
        int userId = AuthorizedUser.id();
        LOG.info("getBetween dates({} - {}) for User {}", startDateTime, endDateTime, userId);
        startDateTime = startDateTime == null ? DateTimeUtil.MIN_DATETIME : startDateTime;
        endDateTime = endDateTime == null ? DateTimeUtil.MAX_DATETIME : endDateTime;
        LocalDateTime startDateTimeStartDay = startDateTime.truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endDateTimeEndDay = endDateTime.truncatedTo(ChronoUnit.DAYS).plusDays(1).minusNanos(1);
        return MealsUtil.getFilteredWithExceeded(
                service.getBetweenDateTimes(startDateTimeStartDay, endDateTimeEndDay, userId),
                startDateTime, endDateTime,
                AuthorizedUser.getCaloriesPerDay()
        );
    }

}