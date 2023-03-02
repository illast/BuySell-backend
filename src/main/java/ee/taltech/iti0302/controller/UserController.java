package ee.taltech.iti0302.controller;

import ee.taltech.iti0302.dto.UserDto;
import ee.taltech.iti0302.repository.user.UserBalanceRequest;
import ee.taltech.iti0302.security.LoginRequest;
import ee.taltech.iti0302.security.LoginResponse;
import ee.taltech.iti0302.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/api/users")
    public List<UserDto> getUsers() {
        log.info("Getting users by GetMapping /api/users");
        return userService.getUsers();
    }

    @GetMapping("/api/users/{id}")
    public UserDto getUserById(@PathVariable("id") Integer id) {
        log.info("Getting user by id by GetMapping /api/users/{}", id);
        return userService.getUserById(id);
    }

    @PostMapping("/api/public/users")
    public void createUser(@RequestBody UserDto userDto) {
        log.info("Saving user by PostMapping /api/public/users");
        userService.createUser(userDto);
    }

    @PostMapping("/api/public/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        log.info("Logging in user by PostMapping /api/public/login");
        return userService.login(request.getEmail(), request.getPassword());
    }

    @PutMapping("/api/users/{id}")
    public void changeBalance(@RequestBody UserBalanceRequest userBalanceRequest, @PathVariable("id") Integer id) {
        log.info("Updating user by id by PutMapping /api/users/{}", id);
        userService.changeBalance(userBalanceRequest, id);
    }
}
