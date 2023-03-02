package ee.taltech.iti0302.service;

import ee.taltech.iti0302.exception.ApplicationException;
import ee.taltech.iti0302.model.Image;
import ee.taltech.iti0302.repository.image.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;
    public static final String EXCEPTION_IMAGE_NOT_FOUND_MESSAGE = "Image not found";

    public void saveImage(MultipartFile file) throws IOException {
        Image image = Image.builder()
                .name(file.getName())
                .originalFileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .size(file.getSize())
                .bytes(file.getBytes())
                .build();
        log.info("Saving image {}", image);
        imageRepository.save(image);
    }

    public ResponseEntity<Object> getImageById(Integer imageId) {
        Optional<Image> imageOptional = imageRepository.findById(imageId);
        Image image = imageOptional.orElseThrow(() -> new ApplicationException(EXCEPTION_IMAGE_NOT_FOUND_MESSAGE));
        log.info("Getting image {} by id {}", image, imageId);
        return ResponseEntity.ok()
                .header("fileName", image.getOriginalFileName())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }
}
