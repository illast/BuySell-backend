package ee.taltech.iti0302.service;

import ee.taltech.iti0302.scheduler.dto.Meme;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MemeServiceTest {

    @InjectMocks
    private MemeService memeService;

    @Test
    void memeServiceSetUrl() {
        memeService.setMemeUrl("test");
        assertEquals("test", memeService.getMemeUrl());
    }
}