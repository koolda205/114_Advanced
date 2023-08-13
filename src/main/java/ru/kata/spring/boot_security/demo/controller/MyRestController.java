package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class MyRestController {

    private final UserService userService;
    private final RoleService roleService;
    private final UserValidator userValidator;

    @Autowired
    public MyRestController(UserService userService, RoleService roleService, UserValidator userValidator) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidator = userValidator;
    }

    @GetMapping()
    public List<User> getUser() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User findUsersById(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @ResponseBody
    @GetMapping("/new")
    public String newUser(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("users", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("user", user);
        return "new";
    }


    @ResponseBody
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

    @ResponseBody
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

    @ResponseBody
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {

        userService.deleteUser(id);

        return "redirect:/users";
    }

    @ResponseBody
    @GetMapping("/error-page")
    public String errorPage(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("user", user);

        return "error-page";
    }

    @ResponseBody
    @GetMapping("/user-info")
    public String showUserInfo(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "user-info";
    }

}