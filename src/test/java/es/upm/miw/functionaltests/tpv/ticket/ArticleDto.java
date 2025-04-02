package es.upm.miw.functionaltests.tpv.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {
    private UUID id;
    private String barcode;
    private String description;
    private BigDecimal retailPrice;

}