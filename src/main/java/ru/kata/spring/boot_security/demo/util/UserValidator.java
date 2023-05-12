package ru.kata.spring.boot_security.demo.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.Optional;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        long id = user.getId();

        Optional<User> userFoundByUsername = userService.findByUsername(user.getUsername());
        if (userFoundByUsername.isPresent() && (id == 0 || userFoundByUsername.get().getId() != id)) {
            errors.rejectValue("username", "", "Этот username уже занят");
        }

        Optional<User> userFoundByEmail = userService.findByEmail(user.getEmail());
        if (userFoundByEmail.isPresent() && (id == 0 || userFoundByEmail.get().getId() != id)) {
            errors.rejectValue("email", "", "Этот email привязан к другой учётке");
        }

    }
}
