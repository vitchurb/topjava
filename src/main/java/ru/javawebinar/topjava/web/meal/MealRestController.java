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

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        LocalDateTime startDateTime1 = startDateTime == null ? DateTimeUtil.MIN_DATETIME : startDateTime;
        LocalDateTime endDateTime1 = endDateTime == null ? DateTimeUtil.MAX_DATETIME : endDateTime;
        LocalDateTime endDateTimeEndDay = endDateTime1.plusDays(1);
        return super.getBetween(startDateTime1.toLocalDate(), null, endDateTimeEndDay.toLocalDate(), null)
                .stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getDateTime(), startDateTime1, endDateTime1))
                .collect(Collectors.toList());
    }

}