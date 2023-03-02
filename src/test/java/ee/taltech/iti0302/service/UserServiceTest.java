package ee.taltech.iti0302.service;

import ee.taltech.iti0302.dto.UserDto;
import ee.taltech.iti0302.mapper.UserMapper;
import ee.taltech.iti0302.mapper.UserMapperImpl;
import ee.taltech.iti0302.model.Product;
import ee.taltech.iti0302.model.Trade;
import ee.taltech.iti0302.model.User;
import ee.taltech.iti0302.repository.user.UserBalanceRequest;
import ee.taltech.iti0302.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Spy
    private UserMapper userMapper = new UserMapperImpl();

    @InjectMocks
    private UserService userService;

    @Test
    void getUsersExist() {
        // given
        User user1 = User.builder().id(1).firstName("Ilja").lastName("Lastovko").build();
        User user2 = User.builder().id(2).firstName("Alan").lastName("Parik").build();
        User user3 = User.builder().id(3).firstName("Mart").lastName("Hütt").build();
        User user4 = User.builder().id(4).firstName("Siim").lastName("Rebane").build();
        List<User> users = new ArrayList<>(List.of(user1, user2, user3, user4));
        given(userRepository.findAll()).willReturn(users);

        // when
        var result = userService.getUsers();

        // then
        then(userRepository).should().findAll();
        then(userMapper).should().toDtoList(users);
        UserDto userDto1 = UserDto.builder().id(1).firstName("Ilja").lastName("Lastovko").buyerTradesAmount(0).sellerTradesAmount(0).build();
        UserDto userDto2 = UserDto.builder().id(2).firstName("Alan").lastName("Parik").buyerTradesAmount(0).sellerTradesAmount(0).build();
        UserDto userDto3 = UserDto.builder().id(3).firstName("Mart").lastName("Hütt").buyerTradesAmount(0).sellerTradesAmount(0).build();
        UserDto userDto4 = UserDto.builder().id(4).firstName("Siim").lastName("Rebane").buyerTradesAmount(0).sellerTradesAmount(0).build();
        List<UserDto> userDtos = new ArrayList<>(List.of(userDto1, userDto2, userDto3, userDto4));
        assertEquals(userDtos, result);
    }

    @Test
    void getUserByIdExists() {
        // given
        User user = User.builder().id(1).firstName("Ilja").lastName("Lastovko").build();
        given(userRepository.findById(1)).willReturn(Optional.of(user));

        // when
        var result = userService.getUserById(1);

        // then
        then(userRepository).should().findById(1);
        then(userMapper).should().entityToDto(user);
        assertEquals(UserDto.builder().id(1).firstName("Ilja").lastName("Lastovko").buyerTradesAmount(0).sellerTradesAmount(0).build(), result);
    }

    @Test
    void createUserIsCorrect() {
        // given
        UserDto userDto = UserDto.builder().id(1).firstName("Ilja").lastName("Lastovko").buyerTradesAmount(0).sellerTradesAmount(0).build();

        User user = User.builder().id(1).firstName("Ilja").lastName("Lastovko").build();

        given(userMapper.dtoToEntity(userDto)).willReturn(user);
        given(userRepository.findById(1)).willReturn(Optional.of(user));

        // when
        userService.createUser(userDto);
        var result = userService.getUserById(1);

        // then
        then(userMapper).should().dtoToEntity(userDto);
        then(userRepository).should().save(user);
        assertEquals(userDto, result);
    }

    @Test
    void changeBalanceIsCorrect() {
        // given
        User user = User.builder().id(1).firstName("Ilja").lastName("Lastovko").balance(100.0).build();
        given(userRepository.findById(1)).willReturn(Optional.of(user));

        // when
        UserBalanceRequest userBalanceRequest = new UserBalanceRequest();
        userBalanceRequest.setBalance(200.0);
        userService.changeBalance(userBalanceRequest, 1);
        UserDto userDto = userService.getUserById(1);
        var result = userDto.getBalance();

        // then
        then(userRepository).should().save(user);
        assertEquals(200, result);
    }
}