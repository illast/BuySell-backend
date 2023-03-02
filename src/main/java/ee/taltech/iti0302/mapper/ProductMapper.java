package ee.taltech.iti0302.mapper;

import ee.taltech.iti0302.dto.ProductDto;
import ee.taltech.iti0302.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    @Mapping(source = "productCategory.name", target = "categoryName")
    ProductDto entityToDto(Product product);

    Product dtoToEntity(ProductDto productDto);

    List<ProductDto> toDtoList(List<Product> products);
}
