package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final UserValidator userValidator;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, UserValidator userValidator) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidator = userValidator;
    }

    @GetMapping
    public String admin(Authentication authentication, Model model, @RequestParam(required = false) String error) {
        User currentUser = (User) authentication.getPrincipal();
        model.addAttribute("current_user", currentUser);
        model.addAttribute("new_user", new User());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", roleService.findAll());
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "admin/admin_panel";
    }

    @PostMapping
    public String save(@ModelAttribute("user") @Valid User user,
                       BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "redirect:/admin?error=error";
        }
        userService.save(user);
        return "redirect:/admin";
    }

    @PatchMapping("/edit")
    public String update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "redirect:/admin?error=error";
        }
        userService.update(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id) {
        User user = userService.findById(id);
        userService.delete(user);
        return "redirect:/admin";
    }

}
