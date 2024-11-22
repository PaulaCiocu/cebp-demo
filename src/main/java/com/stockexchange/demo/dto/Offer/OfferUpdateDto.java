package com.stockexchange.demo.dto.Offer;

import com.stockexchange.demo.entity.Stock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfferUpdateDto {
    private Integer quantity;
    private Double pricePerShare;
    private Boolean isFulfilled;
    private Long stockId;
    private Long sellerId;

}
