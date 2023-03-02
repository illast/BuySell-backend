package ee.taltech.iti0302.scheduler;

import ee.taltech.iti0302.scheduler.dto.Meme;
import ee.taltech.iti0302.service.MemeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class JobSchedulerTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private MemeService memeService;

    @InjectMocks
    private JobScheduler jobScheduler;

    @Test
    void getMemeUrl() {

        // given
        Meme meme = new Meme();
        meme.setMessage("https://dog.ceo/api/breeds/image/random");
        given(restTemplate.getForObject(meme.getMessage(), Meme.class)).willReturn(meme);

        // when
        jobScheduler.getMemeUrl();

        // then
        then(memeService).should().setMemeUrl(meme.getMessage());
    }
}