package ee.taltech.iti0302.controller;

import ee.taltech.iti0302.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/api/public/images")
    public void saveImage(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("Saving image by PostMapping /api/public/images");
        imageService.saveImage(file);
    }

    @GetMapping("/api/public/images/{imageId}")
    public ResponseEntity<Object> getImageById(@PathVariable("imageId") Integer imageId) {
        log.info("Getting image by id by GetMapping /api/public/images/{}", imageId);
        return imageService.getImageById(imageId);
    }
}
