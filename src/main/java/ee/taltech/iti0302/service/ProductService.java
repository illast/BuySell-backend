package ee.taltech.iti0302.service;

import ee.taltech.iti0302.dto.ProductDto;
import ee.taltech.iti0302.exception.ApplicationException;
import ee.taltech.iti0302.mapper.ProductMapper;
import ee.taltech.iti0302.model.Image;
import ee.taltech.iti0302.model.Product;
import ee.taltech.iti0302.model.ProductCategory;
import ee.taltech.iti0302.model.User;
import ee.taltech.iti0302.repository.image.ImageRepository;
import ee.taltech.iti0302.repository.product.ProductCategoryRepository;
import ee.taltech.iti0302.repository.product.ProductRepository;
import ee.taltech.iti0302.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static ee.taltech.iti0302.service.UserService.EXCEPTION_USER_NOT_FOUND_MESSAGE;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private static final int PAGE_SIZE = 10;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductMapper productMapper;
    public static final String EXCEPTION_PRODUCT_NOT_FOUND_MESSAGE = "Product not found";
    public static final String EXCEPTION_PRODUCT_CATEGORY_NOT_FOUND_MESSAGE = "Product category not found";

    public List<ProductDto> getProducts() {
        List<Product> products = productRepository.findAll();
        log.info("Getting {} products", products.size());
        return productMapper.toDtoList(products);
    }

    public ProductDto getProductById(Integer id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        Product product = optionalProduct.orElseThrow(() -> new ApplicationException(EXCEPTION_PRODUCT_NOT_FOUND_MESSAGE));
        log.info("Getting product {} by id {}", product, id);
        return productMapper.entityToDto(product);
    }

    public List<ProductDto> getProductsByUserId(Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() -> new ApplicationException(EXCEPTION_USER_NOT_FOUND_MESSAGE));
        List<Product> products = user.getProducts();
        log.info("Getting {} products by user id {}", products.size(), userId);
        return productMapper.toDtoList(products);
    }

    public void addProduct(ProductDto productDto) {
        Product product = productMapper.dtoToEntity(productDto);
        Optional<ProductCategory> optionalProductCategory = productCategoryRepository.findProductCategoryByName(productDto.getCategoryName());
        ProductCategory productCategory = optionalProductCategory.orElseThrow(() -> new ApplicationException(EXCEPTION_PRODUCT_CATEGORY_NOT_FOUND_MESSAGE));
        product.setCategoryId(productCategory.getId());

        Image image = imageRepository.findTopByOrderByIdDesc();
        product.setImageId(image.getId());

        log.info("Saving product {}", product);
        productRepository.save(product);
    }

    public void deleteProductById(Integer id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        Product product = optionalProduct.orElseThrow(() -> new ApplicationException(EXCEPTION_PRODUCT_NOT_FOUND_MESSAGE));
        log.info("Removing product {}", product);
        productRepository.deleteById(product.getId());
    }

    public void updateProductById(ProductDto productDto, Integer id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) throw new ApplicationException(EXCEPTION_PRODUCT_NOT_FOUND_MESSAGE);
        Product product = productMapper.dtoToEntity(productDto);
        Optional<ProductCategory> optionalProductCategory = productCategoryRepository.findProductCategoryByName(productDto.getCategoryName());
        ProductCategory productCategory = optionalProductCategory.orElseThrow(() -> new ApplicationException(EXCEPTION_PRODUCT_CATEGORY_NOT_FOUND_MESSAGE));
        product.setCategoryId(productCategory.getId());
        log.info("Updating product {}", product);
        productRepository.save(product);
    }

    public List<ProductDto> paginateProductsByUserIdByTradeIdIsNotNull(int page, String orderBy, Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() -> new ApplicationException(EXCEPTION_USER_NOT_FOUND_MESSAGE));
        Sort sort = Sort.by(orderBy).descending();
        Pageable pageRequest = PageRequest.of(page, PAGE_SIZE, sort);
        List<Product> products = productRepository.findAllByUserIdAndTradeIdIsNull(user.getId(), pageRequest);
        log.info("Getting {} products where trade id is not null by user id {}", products.size(), userId);
        return productMapper.toDtoList(products);
    }

    public List<ProductDto> paginateProductsByTradeIdIsNotNull(int page, String orderBy) {
        Sort sort = Sort.by(orderBy).descending();
        Pageable pageRequest = PageRequest.of(page, PAGE_SIZE, sort);
        List<Product> products = productRepository.findAllByTradeIdIsNull(pageRequest);
        log.info("Getting {} products where trade id is not null", products.size());
        return productMapper.toDtoList(products);
    }
}
