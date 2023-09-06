package ru.kata.spring.boot_security.demo.Init;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.annotation.PostConstruct;
import java.util.Set;

@Component
public class DbInit {
    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;

    public DbInit(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }
    @PostConstruct
    private void postConstruct() {
        Role roleAdmin = new Role((long)1,"ADMIN");
        Role roleUser = new Role( (long)2,"USER");
        roleService.addRole(roleAdmin);
        roleService.addRole(roleUser);

        User user = new User( "user", "user", 22, "user" , "user@mail.ru", Set.of(roleUser));
        User admin = new User( "admin","admin", 33, "123","admin@mail.ru",  Set.of(roleAdmin, roleUser));
        User ad = new User( "ad","ad", 44, "ad", "ad@ad.ad",  Set.of(roleAdmin, roleUser));
        userService.addUser(user);
        userService.addUser(admin);
        userService.addUser(ad);
    }
}
