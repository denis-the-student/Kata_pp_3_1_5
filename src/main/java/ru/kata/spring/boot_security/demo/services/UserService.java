package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.entities.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    User findByUsername(String username);

    User findByEmail(String email);

    User findById(long id);

    List<User> findAll();

    User save(User user);

    User update(User user);

    void delete(User user);
}
