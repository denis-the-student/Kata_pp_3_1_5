package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@ControllerAdvice
public class WhitelabelErrorController implements ErrorController {

    @ExceptionHandler(Exception.class)
    @RequestMapping("/error")
    public String handleError() {
        // Return the name of your custom error page
        return "/error";
    }

}
