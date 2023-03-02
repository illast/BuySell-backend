package ee.taltech.iti0302.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class TradeDto {

    private Integer id;
    private Integer buyerId;
    private Integer sellerId;
    private LocalDate date;
}
