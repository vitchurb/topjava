package ru.javawebinar.topjava.dao;

/**
 * Created by vit on 26.03.2017.
 */
public class MealDAOFactory {
    private static class StaticHolder {
        static final MealDAOMemoryImpl mealDAOMemoryImpl = new MealDAOMemoryImpl();
    }

    public static MealDAO getMealDAO() {
        return StaticHolder.mealDAOMemoryImpl;
    }


}
