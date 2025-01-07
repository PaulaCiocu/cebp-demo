package com.stockexchange.demo.dto.Offer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfferDto {
    private Integer quantity;
    private Double pricePerShare;
    private Long stockId;
    private Long userId;
    private Long transactionId;
}