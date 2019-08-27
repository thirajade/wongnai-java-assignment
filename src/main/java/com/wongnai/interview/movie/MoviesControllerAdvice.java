package com.wongnai.interview.movie;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

@RestControllerAdvice(basePackages = {"com.wongnai.interview.movie"})
public class MoviesControllerAdvice {


    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();
        Iterator<ConstraintViolation<?>> iterator = ex.getConstraintViolations().iterator();
        while (iterator.hasNext()) {
            ConstraintViolation<?> cv = iterator.next();
            errors.add(cv.getMessage());
        }
        return ResponseEntity.status(400).body(errors);
    }
}
