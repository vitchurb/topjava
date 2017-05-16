package ru.javawebinar.topjava.annotation;

import java.lang.annotation.*;

/**
 * Created by vit on 15.05.2017.
 */
@Target(value = {ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomDateTime {
    String value() default "yyyy-MM-dd HH:mm";
}
