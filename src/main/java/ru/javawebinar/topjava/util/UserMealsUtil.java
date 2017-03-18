package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    //максимальное количество дней между минимальной и максимальной датой в mealList,
    //для которого будет использоваться алгоритм без Map
    private static final int MAX_DAYS_BETWEEN_DATES = 10_000_000;

    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        List<UserMealWithExceed> lst = getFilteredWithExceededStreams(mealList,
                LocalTime.of(7, 3), LocalTime.of(12, 6), 2000);
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime,
                                                                   LocalTime endTime, int caloriesPerDay) {
        if (mealList == null || mealList.isEmpty())
            return new ArrayList<>();
        if (startTime == null)
            startTime = LocalTime.MIN;
        if (endTime == null)
            endTime = LocalTime.MAX;
        //сумма калорий, сгруппированная по дате
        HashMap<LocalDate, Integer> mapByDays = new HashMap<>();
        for (UserMeal meal : mealList) {
            if (meal != null && meal.getDateTime() != null) {
                mapByDays.merge(meal.getDateTime().toLocalDate(),
                        meal.getCalories(), Integer::sum);
            }
        }
        List<UserMealWithExceed> resultList = new ArrayList<>();
        for (UserMeal meal : mealList) {
            if (meal != null && meal.getDateTime() != null &&
                    TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                resultList.add(new UserMealWithExceed(meal.getDateTime(), meal.getDescription(),
                        meal.getCalories(),
                        mapByDays.getOrDefault(meal.getDateTime().toLocalDate(), 0) > caloriesPerDay));
            }
        }
        return resultList;
    }

    public static List<UserMealWithExceed> getFilteredWithExceededStreams(List<UserMeal> mealList, LocalTime startTime,
                                                                          LocalTime endTime, int caloriesPerDay) {
        if (mealList == null || mealList.isEmpty())
            return new ArrayList<>();
        LocalTime startTimeChecked = startTime == null ? LocalTime.MIN : startTime;
        LocalTime endTimeChecked = endTime == null ? LocalTime.MAX : endTime;
        //сумма калорий, сгруппированная по дате
        Map<LocalDate, Integer> mapByDays = mealList.stream()
                .filter(meal -> Objects.nonNull(meal) && Objects.nonNull(meal.getDateTime()))
                .collect(Collectors.groupingBy(p -> p.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)));
        return mealList.stream()
                .filter(meal -> Objects.nonNull(meal) && Objects.nonNull(meal.getDateTime()) &&
                        TimeUtil.isBetween(meal.getDateTime().toLocalTime(),
                                startTimeChecked, endTimeChecked))
                .map(meal -> new UserMealWithExceed(meal.getDateTime(), meal.getDescription(),
                        meal.getCalories(), mapByDays.getOrDefault(meal.getDateTime().toLocalDate(),
                        0) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    //вариант без использования Map (чтобы не зависеть от коллизий)
    public static List<UserMealWithExceed> getFilteredWithExceededWithoutMap(List<UserMeal> mealList, LocalTime startTime,
                                                                             LocalTime endTime, int caloriesPerDay) {
        if (mealList == null || mealList.isEmpty())
            return new ArrayList<>();
        if (startTime == null)
            startTime = LocalTime.MIN;
        if (endTime == null)
            endTime = LocalTime.MAX;
        LocalDate minDate = null, maxDate = null;
        //Поиск минимальной и максимальной даты записей из mealList, для которых надо будет суммировать дневные калории
        for (UserMeal meal : mealList) {
            if (meal != null && meal.getDateTime() != null
                    && TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                minDate = minDate == null || minDate.isAfter(meal.getDateTime().toLocalDate()) ?
                        meal.getDateTime().toLocalDate() : minDate;
                maxDate = maxDate == null || maxDate.isBefore(meal.getDateTime().toLocalDate()) ?
                        meal.getDateTime().toLocalDate() : maxDate;
            }
        }
        if (minDate == null)
            return new ArrayList<>();
        long diffDays = DAYS.between(minDate, maxDate) + 1;
        if (diffDays > MAX_DAYS_BETWEEN_DATES) {
            return getFilteredWithExceeded(mealList, startTime, endTime, caloriesPerDay);
        }
        //сумма калорий, сгруппированная по дате
        int[] caloriesPerDayArray = new int[(int) diffDays];
        for (UserMeal meal : mealList) {
            if (meal != null && meal.getDateTime() != null &&
                    !meal.getDateTime().toLocalDate().isBefore(minDate) &&
                    !meal.getDateTime().toLocalDate().isAfter(maxDate)) {
                caloriesPerDayArray[(int) DAYS.between(minDate, meal.getDateTime().toLocalDate())] += meal.getCalories();
            }
        }
        List<UserMealWithExceed> resultList = new ArrayList<>();
        for (UserMeal meal : mealList) {
            if (meal != null && meal.getDateTime() != null &&
                    TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                resultList.add(new UserMealWithExceed(meal.getDateTime(), meal.getDescription(),
                        meal.getCalories(),
                        caloriesPerDayArray[(int) DAYS.between(minDate, meal.getDateTime().toLocalDate())]
                                > caloriesPerDay));
            }
        }
        return resultList;
    }

    //вариант без использования Map (чтобы не зависеть от коллизий)
    public static List<UserMealWithExceed> getFilteredWithExceededWithoutMapStreams(List<UserMeal> mealList, LocalTime startTime,
                                                                                    LocalTime endTime, int caloriesPerDay) {
        if (mealList == null || mealList.isEmpty())
            return new ArrayList<>();
        LocalTime startTimeChecked = startTime == null ? LocalTime.MIN : startTime;
        LocalTime endTimeChecked = endTime == null ? LocalTime.MAX : endTime;

        //Поиск минимальной и максимальной даты записей из mealList, для которых надо будет суммировать дневные калории
        Comparator<UserMeal> comparator = Comparator.comparing(meal -> meal.getDateTime().toLocalDate());

        UserMeal minDateMeal = mealList.stream()
                .filter(meal -> Objects.nonNull(meal) && Objects.nonNull(meal.getDateTime()) &&
                        TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTimeChecked, endTimeChecked))
                .min(comparator)
                .orElse(null);
        UserMeal maxDateMeal = mealList.stream()
                .filter(meal -> Objects.nonNull(meal) && Objects.nonNull(meal.getDateTime()) &&
                        TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTimeChecked, endTimeChecked))
                .max(comparator)
                .orElse(null);

        LocalDate minDate = minDateMeal == null ? null : minDateMeal.getDateTime().toLocalDate();
        LocalDate maxDate = maxDateMeal == null ? null : maxDateMeal.getDateTime().toLocalDate();
        if (minDate == null || maxDate == null)
            return new ArrayList<>();
        long diffDays = DAYS.between(minDate, maxDate) + 1;
        if (diffDays > MAX_DAYS_BETWEEN_DATES) {
            return getFilteredWithExceededStreams(mealList, startTime, endTime, caloriesPerDay);
        }

        //сумма калорий, сгруппированная по дате
        int[] caloriesPerDayArray = new int[(int) diffDays];
        mealList.stream()
                .filter(meal -> Objects.nonNull(meal) && Objects.nonNull(meal.getDateTime()) &&
                        !meal.getDateTime().toLocalDate().isBefore(minDate) &&
                        !meal.getDateTime().toLocalDate().isAfter(maxDate))
                .forEach(meal -> caloriesPerDayArray[(int) DAYS.between(minDate, meal.getDateTime().toLocalDate())] += meal.getCalories());

        return mealList.stream()
                .filter(meal -> Objects.nonNull(meal) && Objects.nonNull(meal.getDateTime()) &&
                        TimeUtil.isBetween(meal.getDateTime().toLocalTime(),
                                startTimeChecked, endTimeChecked))
                .map(meal -> new UserMealWithExceed(meal.getDateTime(), meal.getDescription(),
                        meal.getCalories(), caloriesPerDayArray[(int) DAYS.between(minDate, meal.getDateTime().toLocalDate())] > caloriesPerDay))
                .collect(Collectors.toList());
    }


}
