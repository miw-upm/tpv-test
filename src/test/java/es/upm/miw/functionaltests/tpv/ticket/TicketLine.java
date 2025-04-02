package es.upm.miw.functionaltests.tpv.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketLine {

    private ArticleDto articleDto;
    private BigDecimal retailPrice;
    private Integer amount;
    private BigDecimal discount;
    private LineState state;

    public BigDecimal totalUnit() {
        this.discount = discount.setScale(6, RoundingMode.HALF_UP);
        BigDecimal totalUnit = retailPrice.multiply(
                BigDecimal.ONE.subtract(this.discount.divide(new BigDecimal("100"), RoundingMode.HALF_UP)));
        return totalUnit.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal totalShopping() {
        return totalUnit().multiply(new BigDecimal(amount));
    }

}
