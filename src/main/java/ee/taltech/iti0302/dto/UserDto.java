package ee.taltech.iti0302.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class UserDto {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private LocalDate birthdate;
    private String gender;
    private Double balance;
    private Integer buyerTradesAmount;
    private Integer sellerTradesAmount;
}
