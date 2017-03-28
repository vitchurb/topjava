package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Created by vit on 26.03.2017.
 */
public class MealDAOMemoryImpl implements MealDAO {
    private static final Map<Long, Meal> mealsMap = new ConcurrentHashMap<>();
    private static AtomicLong lastGeneratedId = new AtomicLong(100);

    static {
        mealsMap.put(1L, new Meal(1L, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        mealsMap.put(3L, new Meal(3L, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        mealsMap.put(5L, new Meal(5L, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        mealsMap.put(6L, new Meal(6L, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 100));
        mealsMap.put(7L, new Meal(7L, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 50));
        mealsMap.put(8L, new Meal(8L, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 51));
    }

    public void save(Meal meal) {
        if (meal.getId() == null) {
            meal.setId(lastGeneratedId.incrementAndGet());
        }
        mealsMap.put(meal.getId(), meal);
    }

    public Meal getById(Long id) {
        if (id == null)
            return null;
        Meal meal = mealsMap.get(id);
        return meal == null ? null : new Meal(meal);
    }

    public void delete(Long id) {
        if (id != null)
            mealsMap.remove(id);
    }

    public List<Meal> getAll() {
        return mealsMap.values().stream().collect(Collectors.toList());
    }

}
