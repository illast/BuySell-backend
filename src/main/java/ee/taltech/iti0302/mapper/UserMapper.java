package ee.taltech.iti0302.mapper;

import ee.taltech.iti0302.dto.UserDto;
import ee.taltech.iti0302.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "buyerTradesAmount", expression = "java(user.getBuyerTrades()!= null ? user.getBuyerTrades().size():0)")
    @Mapping(target = "sellerTradesAmount", expression = "java(user.getSellerTrades()!= null ? user.getSellerTrades().size():0)")
    UserDto entityToDto(User user);
    User dtoToEntity(UserDto userDto);

    List<UserDto> toDtoList(List<User> users);
}
