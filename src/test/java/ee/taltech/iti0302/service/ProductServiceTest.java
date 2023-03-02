package ee.taltech.iti0302.service;

import ee.taltech.iti0302.dto.ProductDto;
import ee.taltech.iti0302.mapper.ProductMapperImpl;
import ee.taltech.iti0302.model.User;
import ee.taltech.iti0302.exception.ApplicationException;
import ee.taltech.iti0302.mapper.ProductMapper;
import ee.taltech.iti0302.model.*;
import ee.taltech.iti0302.repository.image.ImageRepository;
import ee.taltech.iti0302.repository.product.ProductCategoryRepository;
import ee.taltech.iti0302.repository.product.ProductRepository;
import ee.taltech.iti0302.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    private ProductMapper productMapper = new ProductMapperImpl();

    @InjectMocks
    private ProductService productService;

    @Test
    void getProducts() {
        Product product1 = Product.builder().id(1).name("Milk").description("...").build();
        Product product2 = Product.builder().id(2).name("Milk").description("...").build();
        Product product3 = Product.builder().id(3).name("Milk").description("...").build();
        Product product4 = Product.builder().id(4).name("Milk").description("...").build();
        List<Product> products = new ArrayList<>(List.of(product1, product2, product3, product4));
        given(productRepository.findAll()).willReturn(products);

        // when
        var result = productService.getProducts();

        // then
        then(productMapper).should().toDtoList(products);
        then(productRepository).should().findAll();
        ProductDto productDto1 = ProductDto.builder().id(1).name("Milk").description("...").build();
        ProductDto productDto2 = ProductDto.builder().id(2).name("Milk").description("...").build();
        ProductDto productDto3 = ProductDto.builder().id(3).name("Milk").description("...").build();
        ProductDto productDto4 = ProductDto.builder().id(4).name("Milk").description("...").build();
        List<ProductDto> productDtos = new ArrayList<>(List.of(productDto1, productDto2, productDto3, productDto4));
        assertEquals(productDtos, result);
    }

    @Test
    void getProductByIdExists() {

        // given
        final int PRODUCT_ID_CORRECT = 1;
        Product product = Product.builder().id(PRODUCT_ID_CORRECT).name("Milk").description("smth").build();
        given(productRepository.findById(PRODUCT_ID_CORRECT)).willReturn(Optional.of(product));

        // when
        var result = productService.getProductById(PRODUCT_ID_CORRECT);

        // then
        then(productMapper).should().entityToDto(product);

        ProductDto productDto = ProductDto.builder().id(PRODUCT_ID_CORRECT).name("Milk").description("smth").build();
        assertEquals(productDto, result);
    }

    @Test
    void getProductByIdDoesNotExist() {
        try {
            productService.getProductById(2);
            fail("Should have thrown the exception.");
        }
        catch (ApplicationException ignored) {
        }
    }

    @Test
    void addProductIsCorrect() {

        // given
        int productId = 1, imageId = 1, categoryId =  1, userId = 1, tradeId = 1;
        double productPrice = 50.55;
        String productName = "Banana";
        String productDescription = "smth";

        ProductCategory productCategory = ProductCategory.builder()
                .name("Cat")
                .id(1)
                .build();

        ProductDto productDto = ProductDto.builder()
                .id(productId)
                .name(productName)
                .description(productDescription)
                .price(productPrice)
                .categoryId(categoryId)
                .userId(userId)
                .imageId(imageId)
                .tradeId(tradeId)
                .categoryName(productCategory.getName())
                .build();

        Product product = Product.builder()
                .id(productId)
                .name(productName)
                .description(productDescription)
                .price(productPrice)
                .imageId(imageId)
                .categoryId(categoryId)
                .userId(userId)
                .tradeId(tradeId)
                .build();

        given(productMapper.dtoToEntity(productDto)).willReturn(product);
        given(productCategoryRepository.findProductCategoryByName(productCategory.getName()))
                .willReturn(Optional.of(productCategory));
        given(imageRepository.findTopByOrderByIdDesc()).willReturn(new Image());
        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        // when
        productService.addProduct(productDto);
        var addedProduct = productService.getProductById(productId);
        addedProduct.setCategoryName(productCategory.getName());
        addedProduct.setImageId(imageId);

        // then
        then(productMapper).should().dtoToEntity(productDto);
        then(productRepository).should().save(product);
        assertEquals(productDto, addedProduct);
    }

    @Test
    void deleteProductByExistingId() {

        // given
        int productId = 1, imageId = 1, categoryId =  1, userId = 1, tradeId = 1;
        double productPrice = 50.55;
        String productName = "Banana";
        String productDescription = "smth";

        Product product = Product.builder()
                .id(productId)
                .name(productName)
                .description(productDescription)
                .price(productPrice)
                .imageId(imageId)
                .categoryId(categoryId)
                .userId(userId)
                .tradeId(tradeId)
                .build();

        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        // when
        productService.deleteProductById(productId);

        // then
        then(productRepository).should().deleteById(productId);
    }

    @Test
    void deleteProductByNonExistingId() {

        try {
            productService.deleteProductById(2);
            fail("Should have thrown the exception.");
        } catch (ApplicationException ignored) {
        }
    }

    @Test
    void getProductsByUserId() {

        // given
        Product product1 = Product.builder().id(1).name("Milk").description("...").userId(1).build();
        Product product2 = Product.builder().id(2).name("Milk").description("...").userId(1).build();
        List<Product> products = new ArrayList<>(List.of(product1, product2));

        User user1 = User.builder().id(1).products(products).build();

        given(userRepository.findById(1)).willReturn(Optional.of(user1));

        // when
        var result = productService.getProductsByUserId(1);

        // then
        then(userRepository).should().findById(1);
        then(productMapper).should().toDtoList(products);

        ProductDto productDto1 = ProductDto.builder().id(1).name("Milk").description("...").userId(1).build();
        ProductDto productDto2 = ProductDto.builder().id(2).name("Milk").description("...").userId(1).build();
        List<ProductDto> productDtos = new ArrayList<>(List.of(productDto1, productDto2));

        assertEquals(result, productDtos);
    }

    @Test
    void updateProductById() {

        // given
        Product product1 = Product.builder().id(1).name("Milk").description("...").build();
        ProductDto productDto1 = ProductDto.builder().id(1).name("Jam").categoryName("name").description("...").build();
        given(productRepository.findById(product1.getId())).willReturn(Optional.of(product1));

        Product newProduct = Product.builder().id(1).build();
        given(productMapper.dtoToEntity(productDto1)).willReturn(newProduct);
        ProductCategory productCategory = ProductCategory.builder().build();
        given(productCategoryRepository.findProductCategoryByName(productDto1.getCategoryName())).willReturn(Optional.of(productCategory));

        // when
        productService.updateProductById(productDto1, product1.getId());

        // then
        then(productMapper).should().dtoToEntity(productDto1);
        then(productRepository).should().save(newProduct);
    }

    @Test
    void paginateProductsByUserIdByTradeIdIsNotNull() {

        // given
        Product product1 = Product.builder().id(1).name("Milk").description("...").userId(1).build();
        Product product2 = Product.builder().id(2).name("Milk").description("...").userId(1).build();
        Product product3 = Product.builder().id(3).name("Milk").description("...").userId(1).build();
        Product product4 = Product.builder().id(4).name("Milk").description("...").userId(1).build();
        Product product5 = Product.builder().id(5).name("Milk").description("...").userId(1).build();
        Product product6 = Product.builder().id(6).name("Milk").description("...").userId(1).build();
        Product product7 = Product.builder().id(7).name("Milk").description("...").userId(1).build();
        Product product8 = Product.builder().id(8).name("Milk").description("...").userId(1).build();
        Product product9 = Product.builder().id(9).name("Milk").description("...").userId(1).build();
        Product product10 = Product.builder().id(10).name("Milk").description("...").userId(1).build();
        List<Product> products = new ArrayList<>(List.of(product1, product2, product3, product4, product5, product6, product7, product8, product9, product10));

        User user1 = User.builder().id(1).products(products).build();

        Sort sort = Sort.by("id").descending();
        Pageable pageRequest = PageRequest.of(0, 10, sort);

        given(userRepository.findById(user1.getId())).willReturn(Optional.of(user1));
        given(productRepository.findAllByUserIdAndTradeIdIsNull(user1.getId(), pageRequest)).willReturn(products);

        // when
        var result = productService.paginateProductsByUserIdByTradeIdIsNotNull(0, "id", user1.getId());

        // then
        then(userRepository).should().findById(user1.getId());
        then(productRepository).should().findAllByUserIdAndTradeIdIsNull(user1.getId(), pageRequest);

        ProductDto productDto1 = ProductDto.builder().id(1).name("Milk").description("...").userId(1).build();
        ProductDto productDto2 = ProductDto.builder().id(2).name("Milk").description("...").userId(1).build();
        ProductDto productDto3 = ProductDto.builder().id(3).name("Milk").description("...").userId(1).build();
        ProductDto productDto4 = ProductDto.builder().id(4).name("Milk").description("...").userId(1).build();
        ProductDto productDto5 = ProductDto.builder().id(5).name("Milk").description("...").userId(1).build();
        ProductDto productDto6 = ProductDto.builder().id(6).name("Milk").description("...").userId(1).build();
        ProductDto productDto7 = ProductDto.builder().id(7).name("Milk").description("...").userId(1).build();
        ProductDto productDto8 = ProductDto.builder().id(8).name("Milk").description("...").userId(1).build();
        ProductDto productDto9 = ProductDto.builder().id(9).name("Milk").description("...").userId(1).build();
        ProductDto productDto10 = ProductDto.builder().id(10).name("Milk").description("...").userId(1).build();
        List<ProductDto> productDtos = new ArrayList<>(List.of(productDto1, productDto2, productDto3, productDto4, productDto5, productDto6, productDto7, productDto8, productDto9, productDto10));
        assertEquals(result, productDtos);
    }

    @Test
    void paginateProductsByTradeIdIsNotNull() {
        // given
        Product product1 = Product.builder().id(1).name("Milk").description("...").userId(1).build();
        Product product2 = Product.builder().id(2).name("Milk").description("...").userId(1).build();
        Product product3 = Product.builder().id(3).name("Milk").description("...").userId(1).build();
        Product product4 = Product.builder().id(4).name("Milk").description("...").userId(1).build();
        Product product5 = Product.builder().id(5).name("Milk").description("...").userId(1).build();
        Product product6 = Product.builder().id(6).name("Milk").description("...").userId(1).build();
        Product product7 = Product.builder().id(7).name("Milk").description("...").userId(1).build();
        Product product8 = Product.builder().id(8).name("Milk").description("...").userId(1).build();
        Product product9 = Product.builder().id(9).name("Milk").description("...").userId(1).build();
        Product product10 = Product.builder().id(10).name("Milk").description("...").userId(1).build();
        List<Product> products = new ArrayList<>(List.of(product1, product2, product3, product4, product5, product6, product7, product8, product9, product10));

        Sort sort = Sort.by("id").descending();
        Pageable pageRequest = PageRequest.of(0, 10, sort);

        given(productRepository.findAllByTradeIdIsNull(pageRequest)).willReturn(products);

        // when
        var result = productService.paginateProductsByTradeIdIsNotNull(0, "id");

        // then
        then(productRepository).should().findAllByTradeIdIsNull(pageRequest);

        ProductDto productDto1 = ProductDto.builder().id(1).name("Milk").description("...").userId(1).build();
        ProductDto productDto2 = ProductDto.builder().id(2).name("Milk").description("...").userId(1).build();
        ProductDto productDto3 = ProductDto.builder().id(3).name("Milk").description("...").userId(1).build();
        ProductDto productDto4 = ProductDto.builder().id(4).name("Milk").description("...").userId(1).build();
        ProductDto productDto5 = ProductDto.builder().id(5).name("Milk").description("...").userId(1).build();
        ProductDto productDto6 = ProductDto.builder().id(6).name("Milk").description("...").userId(1).build();
        ProductDto productDto7 = ProductDto.builder().id(7).name("Milk").description("...").userId(1).build();
        ProductDto productDto8 = ProductDto.builder().id(8).name("Milk").description("...").userId(1).build();
        ProductDto productDto9 = ProductDto.builder().id(9).name("Milk").description("...").userId(1).build();
        ProductDto productDto10 = ProductDto.builder().id(10).name("Milk").description("...").userId(1).build();
        List<ProductDto> productDtos = new ArrayList<>(List.of(productDto1, productDto2, productDto3, productDto4, productDto5, productDto6, productDto7, productDto8, productDto9, productDto10));
        assertEquals(result, productDtos);
    }
}