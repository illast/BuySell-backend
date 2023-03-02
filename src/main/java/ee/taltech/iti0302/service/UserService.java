package ee.taltech.iti0302.service;

import ee.taltech.iti0302.dto.UserDto;
import ee.taltech.iti0302.exception.ApplicationException;
import ee.taltech.iti0302.exception.AuthorizationException;
import ee.taltech.iti0302.mapper.UserMapper;
import ee.taltech.iti0302.model.User;
import ee.taltech.iti0302.repository.user.UserBalanceRequest;
import ee.taltech.iti0302.repository.user.UserRepository;
import ee.taltech.iti0302.security.JwtTokenProvider;
import ee.taltech.iti0302.security.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public static final String EXCEPTION_USER_NOT_FOUND_MESSAGE = "User not found";

    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();
        log.info("Getting {} users", users.size());
        return userMapper.toDtoList(users);
    }

    public UserDto getUserById(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        User user = optionalUser.orElseThrow(() -> new ApplicationException(EXCEPTION_USER_NOT_FOUND_MESSAGE));
        log.info("Getting user {} by id {}", user, id);
        return userMapper.entityToDto(user);
    }

    public void createUser(UserDto userDto) {
        User user = userMapper.dtoToEntity(userDto);
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        log.info("Saving user {}", user);
        userRepository.save(user);
    }

    public LoginResponse login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.orElseThrow(() -> new ApplicationException(EXCEPTION_USER_NOT_FOUND_MESSAGE));
        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            log.info("Logging in user {} with email {}", user, email);
            return new LoginResponse(JwtTokenProvider.generateToken(email, user.getId()));
        }
        throw new AuthorizationException("Wrong email or password");
    }

    public void changeBalance(UserBalanceRequest userBalanceRequest, Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        User user = optionalUser.orElseThrow(() -> new ApplicationException(EXCEPTION_USER_NOT_FOUND_MESSAGE));
        Double balance = userBalanceRequest.getBalance();
        user.setBalance(balance);
        log.info("Updating user {} balance to {}", user, balance);
        userRepository.save(user);
    }
}
