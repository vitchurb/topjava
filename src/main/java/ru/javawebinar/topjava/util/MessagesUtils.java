package ru.javawebinar.topjava.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.BindingResult;

/**
 * Created by vit on 31.05.2017.
 */
public class MessagesUtils {
    @Autowired
    MessageSource messageSource;

    public String describeErrors(BindingResult bindingResult) {
        StringBuilder sb = new StringBuilder();
        bindingResult.getGlobalErrors().forEach(ge -> sb.append(messageSource.getMessage(ge, LocaleContextHolder.getLocale()))
                .append("<br>"));
        bindingResult.getFieldErrors().forEach(fe -> sb.append(messageSource.getMessage(fe, LocaleContextHolder.getLocale()))
                .append("<br>"));
        return sb.toString();
    }
}
