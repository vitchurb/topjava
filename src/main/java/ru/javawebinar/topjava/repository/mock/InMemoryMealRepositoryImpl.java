package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * GKislin
 * 15.09.2015.
 */
@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.getUserId() == null)
            return null;
        boolean isNew = meal.isNew();
        if (isNew) {
            meal.setId(counter.incrementAndGet());
        }

        Meal mealDB = repository.get(meal.getId());
        if (mealDB == null && isNew) {
            repository.put(meal.getId(), meal);
            return meal;
        } else if (mealDB != null && !isNew) {
            if (mealDB.getUserId().equals(meal.getUserId())) {
                repository.put(meal.getId(), meal);
                return meal;
            }
        }
        return null;
    }

    @Override
    public boolean delete(int id, Integer userId) {
        return userId != null && repository.entrySet()
                .removeIf(entry -> entry.getKey().equals(id) &&
                        Objects.equals(entry.getValue().getUserId(), userId));
    }

    @Override
    public Meal get(int id, Integer userId) {
        Meal meal = repository.get(id);
        return meal == null ? null : (meal.getUserId().equals(userId) ? meal : null);
    }

    @Override
    public Collection<Meal> getAll(Integer userId) {
        return getAllFilteredByDate(userId, null, null);
    }

    @Override
    public Collection<Meal> getAllFilteredByDate(Integer userId, LocalDate dateFrom, LocalDate dateTo) {
        return repository.values().stream()
                .filter(meal -> Objects.equals(meal.getUserId(), userId) &&
                        meal.getDate() != null &&
                        DateTimeUtil.isBetween(meal.getDate(), dateFrom, dateTo))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }


}

