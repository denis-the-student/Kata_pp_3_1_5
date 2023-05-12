package ru.kata.spring.boot_security.demo.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.Set;

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

        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        roleService.save(adminRole);

        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        roleService.save(userRole);

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setEmail("admin@test.test");
        admin.setRoles(Set.of(adminRole));
        userService.save(admin);

        User user = new User();
        user.setUsername("user");
        user.setPassword("user");
        user.setEmail("user@test.test");
        user.setRoles(Set.of(userRole));
        userService.save(user);

        for (int i = 1; i < 5; i++) {
            User testUser = new User();
            testUser.setUsername("test_user" + i);
            testUser.setPassword("1234");
            testUser.setEmail("user" + i + "@test.test");
            testUser.setRoles(Set.of(userRole));
            userService.save(testUser);
        }
    }
}
