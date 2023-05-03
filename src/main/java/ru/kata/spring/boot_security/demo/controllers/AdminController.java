package ru.kata.spring.boot_security.demo.controllers;

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

    public AdminController(UserService userService, RoleService roleService, UserValidator userValidator) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidator = userValidator;
    }

    @GetMapping
    public String admin(Model model) {
        model.addAttribute("users", userService.findAll());
        return "/admin/admin_panel";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.findAll());
        return "/admin/create";
    }

    @PostMapping("/create")
    public String save(@ModelAttribute("user") @Valid User user,
                       BindingResult bindingResult, Model model) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.findAll());
            return "/admin/create";
        }
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") long id) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("roles", roleService.findAll());
        return "/admin/edit";
    }

    @PatchMapping("/edit")
    public String update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult, Model model) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.findAll());
            return "/admin/edit";
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
