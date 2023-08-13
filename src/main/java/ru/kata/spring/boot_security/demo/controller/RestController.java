package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/")
public class RestController {

    private final UserService userService;
    private final RoleService roleService;
    private final UserValidator userValidator;

    @Autowired
    public RestController(UserService userService, RoleService roleService, UserValidator userValidator) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidator = userValidator;
    }

    @GetMapping("/users")
    public String showAllUsers(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("user", userService.getAllUsers());

//        List<User> allUsers = userService.getAllUsers();
//        allUsers.toArray().toString();

        return "allUsers";
    }

    @GetMapping("/new")
    public String newUser(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("users", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("user", user);
        return "new";
    }

    @PostMapping("/addNewUser")
    public String saveUser(@ModelAttribute("user") @Valid User user,
                           BindingResult bindingResult) {

        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "error-page";
        }

        userService.saveUser(user);

        return "redirect:/users";
    }

    @GetMapping("/findUsersById{id}")
    public String findUsersById(@PathVariable("id") Long id,
                                Model model) {

        model.addAttribute("user", userService.getUserById(id));

        return "user-info";
    }

    @PatchMapping("/editUser/{id}")
    public String edit(@ModelAttribute("user") @Valid User user,
                       BindingResult bindingResult,
                       @PathVariable("id") Long id) {

        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "error-page";
        }
        userService.updateUser(id, user);

        return "redirect:/users";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {

        userService.deleteUser(id);

        return "redirect:/users";
    }

    @GetMapping("/error-page")
    public String errorPage(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("user", user);

        return "error-page";
    }

    @GetMapping("/user-info")
    public String showUserInfo(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "user-info";
    }

}