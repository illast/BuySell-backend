package ee.taltech.iti0302.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginResponse {
    private String token;
}
