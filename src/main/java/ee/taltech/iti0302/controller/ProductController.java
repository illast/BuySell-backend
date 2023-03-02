package ee.taltech.iti0302.controller;

import ee.taltech.iti0302.dto.ProductDto;
import ee.taltech.iti0302.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @GetMapping("/api/public/products")
    public List<ProductDto> getProducts() {
        log.info("Getting products by GetMapping /api/public/products");
        return productService.getProducts();
    }

    @GetMapping("/api/public/product/{id}")
    public ProductDto getProductById(@PathVariable("id") Integer id) {
        log.info("Getting product by id by GetMapping /api/public/product/{}", id);
        return productService.getProductById(id);
    }

    @GetMapping("/api/public/products/{userId}")
    public List<ProductDto> getProductsByUserId(@PathVariable("userId") Integer id) {
        log.info("Getting products by user id by GetMapping /api/public/products/{}", id);
        return productService.getProductsByUserId(id);
    }

    @PostMapping("/api/public/products")
    public void addProduct(@RequestBody ProductDto productDto) {
        log.info("Saving product by PostMapping /api/public/products");
        productService.addProduct(productDto);
    }

    @DeleteMapping("/api/public/products/{productId}")
    public void deleteProduct(@PathVariable("productId") Integer id) {
        log.info("Removing product by id by DeleteMapping /api/public/products/{}", id);
        productService.deleteProductById(id);
    }

    @PutMapping("/api/public/products/{productId}")
    public void updateProduct(@RequestBody ProductDto productDto, @PathVariable("productId") Integer id) {
        log.info("Updating product by id by PutMapping /api/public/products/{}", id);
        productService.updateProductById(productDto, id);
    }

    @GetMapping("/api/public/products3")
    public List<ProductDto> paginateProductsByUserIdByTradeIdIsNotNull(int page, String orderBy, Integer userId) {
        log.info("Getting products where trade id is not null by user id by GetMapping /api/public/products3?page={}&orderBy={}&userId={}", page, orderBy, userId);
        return productService.paginateProductsByUserIdByTradeIdIsNotNull(page, orderBy, userId);
    }

    @GetMapping("/api/public/products4")
    public List<ProductDto> paginateProductsByTradeIdIsNotNull(int page, String orderBy) {
        log.info("Getting products where trade id is not null by GetMapping /api/public/products4?page={}&orderBy={}", page, orderBy);
        return productService.paginateProductsByTradeIdIsNotNull(page, orderBy);
    }
}
