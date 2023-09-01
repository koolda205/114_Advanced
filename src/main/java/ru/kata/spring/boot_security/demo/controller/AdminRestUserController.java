package ru.kata.spring.boot_security.demo.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AdminRestUserController {

    private final UserService userService;
    private final RoleService roleService;
    private final UserValidator userValidator;

    private final ModelMapper modelMapper;

    @Autowired
    public AdminRestUserController(UserService userService, RoleService roleService, UserValidator userValidator, ModelMapper modelMapper) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidator = userValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers().stream().map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/users/{id}")
    public UserDTO findUsersById(@PathVariable("id") Long id) {
        return convertToUserDTO(userService.getUserById(id));
    }

    @PostMapping("/users")
    public ResponseEntity<HttpStatus> saveUser(@RequestBody @Valid UserDTO userDTO,
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

        userService.saveUser(convertToUser(userDTO));

        return  ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {

        userService.deleteUser(id);

        return  ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<HttpStatus> editUser (@PathVariable("id") Long id, @RequestBody @Valid UserDTO userDTO,
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

        userService.saveUser(convertToUser(userDTO));

        return  ResponseEntity.ok(HttpStatus.OK);
    }

    private User convertToUser(UserDTO userDTO) {

        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO convertToUserDTO(User user) {

        return modelMapper.map(user, UserDTO.class);
    }
}