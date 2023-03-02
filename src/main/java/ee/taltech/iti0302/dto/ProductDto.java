package ee.taltech.iti0302.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductDto {

    private Integer id;
    private Integer userId;
    private String name;
    private String description;
    private Double price;
    private Integer categoryId;
    private Integer tradeId;
    private String categoryName;
    private Integer imageId;
}
