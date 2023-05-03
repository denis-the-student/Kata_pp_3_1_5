package ru.kata.spring.boot_security.demo.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.UserService;

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

        User foundUserByUsername = userService.findByUsername(user.getUsername());
        if (foundUserByUsername != null && foundUserByUsername.getId() != user.getId()) {
            errors.rejectValue("username", "", "Хуман с таким username уже есть");
        }

        User foundUserByEmail = userService.findByEmail(user.getEmail());
        if (foundUserByEmail != null && foundUserByEmail.getId() != user.getId()) {
            errors.rejectValue("email", "", "Хуман с таким email уже есть");
        }

    }
}
