package ee.taltech.iti0302.repository.product;

import ee.taltech.iti0302.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {

    Optional<ProductCategory> findProductCategoryByName(String name);
}
