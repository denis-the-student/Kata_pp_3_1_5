package ru.kata.spring.boot_security.demo.dto;

import ru.kata.spring.boot_security.demo.entities.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

public class UserDTO {

    private long id;

    @NotEmpty(message = "Обязательное поле")
    @Size(min = 2, message = "Минимум 2 символа")
    private String username;

    @NotEmpty(message = "Обязательное поле")
    @Email(message = "Невалидный адрес")
    private String email;

    @NotEmpty(message = "Обязательное поле")
    @Size(min = 4, message = "Минимум 4 символа")
    private String password;

    @NotEmpty(message = "Обязательное поле")
    private Set<Role> roles;

    public UserDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
