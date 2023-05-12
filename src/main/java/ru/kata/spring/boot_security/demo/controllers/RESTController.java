package ru.kata.spring.boot_security.demo.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.exceptions.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.exceptions.UserNotFoundException;
import ru.kata.spring.boot_security.demo.exceptions.UserNotUpdatedException;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class RESTController {

    private final UserService userService;
    private final RoleService roleService;
    private final UserValidator validator;
    private final ModelMapper modelMapper;

    @Autowired
    public RESTController(UserService userService, RoleService roleService, UserValidator validator, ModelMapper modelMapper) {
        this.userService = userService;
        this.roleService = roleService;
        this.validator = validator;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> showAllUsers() {
        List<User> users = userService.findAll();
        List<UserDTO> userDTOList = users.stream().map(this::convertToUserDTO).collect(Collectors.toList());
        return ResponseEntity.ok(userDTOList);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> showOne(@PathVariable("id") long id) {
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException(String.format("Хумана с id=%d нет в базе", id));
        }
        User user = userOptional.get();
        return ResponseEntity.ok(convertToUserDTO(user));
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> addNew(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        User user = convertToUser(userDTO);
        validator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField()).append(" — ").append(error.getDefaultMessage()).append("; ");
            }
            throw new UserNotCreatedException(errorMsg.toString());
        }
        userService.save(user);
        return ResponseEntity.ok(convertToUserDTO(user));
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable("id") Long id, @RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        User user = convertToUser(userDTO);
        if (userService.findById(id).isEmpty()) {
            throw new UserNotFoundException(String.format("Хумана с id=%s нет в базе", id));
        }
        user.setId(id);
        validator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField()).append(" — ").append(error.getDefaultMessage()).append("; ");
            }
            throw new UserNotUpdatedException(errorMsg.toString());
        }
        userService.save(user);
        return ResponseEntity.ok(convertToUserDTO(user));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable long id) {
        if (userService.findById(id).isEmpty()) {
            throw new UserNotFoundException(String.format("Хумана с id=%d нет в базе", id));
        }
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/current-user")
    public UserDTO getCurrentUser(@AuthenticationPrincipal User user) {
        return convertToUserDTO(user);
    }

    @GetMapping("/roles")
    public  ResponseEntity<List<Role>> showAllRoles() {
        List<Role> roles = roleService.findAll();
        return  ResponseEntity.ok(roles);
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

}
