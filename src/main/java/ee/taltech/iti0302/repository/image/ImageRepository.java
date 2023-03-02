package ee.taltech.iti0302.repository.image;

import ee.taltech.iti0302.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Integer> {

    Image findTopByOrderByIdDesc();
}
