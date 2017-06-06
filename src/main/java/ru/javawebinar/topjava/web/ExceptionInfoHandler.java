package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.util.MessagesUtils;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.ErrorInfo;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.meal.MealAjaxController;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminAjaxController;
import ru.javawebinar.topjava.web.user.AdminRestController;
import ru.javawebinar.topjava.web.user.ProfileRestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {
    private static Logger LOG = LoggerFactory.getLogger(ExceptionInfoHandler.class);
    private static List<String> pathsMealDateTimeError = Arrays.asList(MealRestController.REST_URL, MealAjaxController.URL);
    private static List<String> pathsUserEmailError = Arrays.asList(ProfileRestController.REST_URL, AdminAjaxController.URL,
            AdminRestController.REST_URL);

    @Autowired
    MessagesUtils messagesUtils;

    //  http://stackoverflow.com/a/22358422/548473
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ErrorInfo handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        return logAndGetErrorInfo(req, e, true);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY) //422
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ErrorInfo handleError(HttpServletRequest req, BindException e) {
        return logAndGetErrorInfo(req, e, false);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY) //422
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorInfo handleError(HttpServletRequest req, MethodArgumentNotValidException e) {
        return logAndGetErrorInfo(req, e, false);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true);
    }

    private ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logException) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        if (logException) {
            LOG.error("Exception at request " + req.getRequestURL(), rootCause);
        } else {
            LOG.warn("Exception at request " + req.getRequestURL() + ": " + rootCause.toString());
        }
        if (e instanceof DataIntegrityViolationException) {
            String path = req.getPathInfo() == null ? req.getServletPath() : req.getPathInfo();
            if (pathsMealDateTimeError.stream().anyMatch(path::contains)) {
                return new ErrorInfo(req.getRequestURL(), e, messagesUtils.getMessage("error.meal.dateTime.dublicated"));
            } else if (pathsUserEmailError.stream().anyMatch(path::contains)) {
                return new ErrorInfo(req.getRequestURL(), e, messagesUtils.getMessage("error.user.email.dublicated"));
            }
            return new ErrorInfo(req.getRequestURL(), rootCause, rootCause.getLocalizedMessage());
        }
        if (rootCause instanceof BindingResult) {
            return new ErrorInfo(req.getRequestURL(), rootCause, messagesUtils.describeErrors((BindException) rootCause));
        } else if (rootCause instanceof MethodArgumentNotValidException) {
            return new ErrorInfo(req.getRequestURL(), rootCause,
                    messagesUtils.describeErrors(((MethodArgumentNotValidException) rootCause).getBindingResult()));
        } else {
            return new ErrorInfo(req.getRequestURL(), rootCause, rootCause.getLocalizedMessage());
        }
    }
}