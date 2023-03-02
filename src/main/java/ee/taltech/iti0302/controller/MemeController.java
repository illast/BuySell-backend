package ee.taltech.iti0302.controller;


import ee.taltech.iti0302.service.MemeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemeController {

    private final MemeService memeService;

    @GetMapping("/api/public/meme")
    public String getMeme() {
        log.info("Getting meme by GetMapping /api/public/meme");
        return memeService.getMemeUrl();
    }
}
