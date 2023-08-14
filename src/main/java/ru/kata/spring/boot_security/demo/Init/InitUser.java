package ru.kata.spring.boot_security.demo.Init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;


@Component
public class InitUser {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public InitUser(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    private void dataBaseInit() throws Exception {
        Role roleAdmin = new Role("ADMIN");
        Role roleUser = new Role("USER");
        Set<Role> adminSet = new HashSet<>();
        Set<Role> userSet = new HashSet<>();

        roleService.addRole(roleAdmin);
        roleService.addRole(roleUser);

        adminSet.add(roleAdmin);
        adminSet.add(roleUser);
        userSet.add(roleUser);



        User user = new User("Anatoly", "Petrov", 23, "petrov@mail.ru",
                "user", userSet);

        User user1 = new User("John", "Rogue", 55, "ad@ad.ad",
                "ad", adminSet);

        User user2 = new User("Tom", "Kulakov", 44, "kulakov@gmail.com",
                "admin", adminSet);

        User user3 = new User("admin", "admin", 33, "admin@admin.ru",
                "admin", adminSet);

        User user4 = new User("user", "user", 22, "user@user.ru",
                "user", userSet);

        userService.saveUser(user);
        userService.saveUser(user1);
        userService.saveUser(user2);
        userService.saveUser(user3);
        userService.saveUser(user4);
    }
}
