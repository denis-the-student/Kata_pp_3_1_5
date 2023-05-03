package ru.kata.spring.boot_security.demo.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.Collections;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;

    public DataLoader(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void run(String... args) {
        Role adminRole = new Role("ROLE_ADMIN");
        Role userRole = new Role("ROLE_USER");
        roleService.save(adminRole);
        roleService.save(userRole);

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setEmail("admin@test.test");
        admin.setRoles(Collections.singletonList(adminRole));
        userService.save(admin);

        User user = new User();
        user.setUsername("user");
        user.setPassword("user");
        user.setEmail("user@test.test");
        user.setRoles(Collections.singletonList(userRole));
        userService.save(user);
    }
}
