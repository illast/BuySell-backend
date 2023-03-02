package ee.taltech.iti0302.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Setter @Getter
@RequiredArgsConstructor
@Service
public class MemeService {

    private String memeUrl;
}
