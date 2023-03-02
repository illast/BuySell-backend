package ee.taltech.iti0302.service;

import ee.taltech.iti0302.dto.UserDto;
import ee.taltech.iti0302.model.Image;
import ee.taltech.iti0302.model.Product;
import ee.taltech.iti0302.model.User;
import ee.taltech.iti0302.repository.image.ImageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageService imageService;

    @Test
    void saveImageIsCorrect() throws IOException {
        // given
        Product product = new Product();
        MockMultipartFile file = new MockMultipartFile("name", "originalFileName", "image/jpeg", (byte[]) null);
        Image image = Image.builder().name("name").originalFileName("originalFileName").contentType("image/jpeg").bytes((null)).product(product).build();
        given(imageRepository.findById(1)).willReturn(Optional.of(image));

        // when
        imageService.saveImage(file);
        var result = imageRepository.findById(1).orElseThrow();

        // then
        assertEquals(image, result);
        assertEquals(image.getName(), result.getName());
        assertEquals(image.getProduct(), result.getProduct());
    }

    @Test
    void getImageById() {
        // given
        byte[] array = {1, 2, 3, 4};
        Image image = Image.builder().name("fileName").originalFileName("originalFileName").contentType("image/jpeg").size(10L).bytes(array).build();
        given(imageRepository.findById(1)).willReturn(Optional.of(image));


        // when
        var result = imageService.getImageById(1);

        // then
        then(imageRepository).should().findById(1);
        ResponseEntity<Object> responseEntity = ResponseEntity.ok()
                .header("fileName", image.getOriginalFileName())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
        assertEquals(responseEntity.getStatusCode(), result.getStatusCode());
        assertEquals(responseEntity.getStatusCodeValue(), result.getStatusCodeValue());
        assertEquals(responseEntity.getHeaders(), result.getHeaders());
    }
}