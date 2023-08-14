package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserErrorResponse;
import ru.kata.spring.boot_security.demo.util.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.util.UserNotFoundExeption;
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

    @GetMapping
    public List<User> getUser() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User findUsersById(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

//    @PostMapping
//    public ResponseEntity<HttpStatus> create(@RequestBody @Valid User user,
//                                             BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            StringBuilder errorMsg = new StringBuilder();
//
//            List<FieldError> errors = bindingResult.getFieldErrors();
//            for (FieldError error : errors) {
//                errorMsg.append(error.getField())
//                        .append(" - ").append(error.getDefaultMessage())
//                        .append(";");
//            }
//
//            throw new UserNotCreatedException(errorMsg.toString());
//        }
//
//        userService.saveUser(user);
//
//        return  ResponseEntity.ok(HttpStatus.OK);
//    }

    @PostMapping
    public ResponseEntity<HttpStatus> saveUser(@ModelAttribute("user") @Valid User user,
                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }

            throw new UserNotCreatedException(errorMsg.toString());
        }

        userService.saveUser(user);

        return  ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {

        userService.deleteUser(id);

        return  ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handeExeptions(UserNotFoundExeption e){
        UserErrorResponse response = new UserErrorResponse(
                "User with this id wasn`t found",
                System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handeExeptions(UserNotCreatedException e){
        UserErrorResponse response = new UserErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
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

}