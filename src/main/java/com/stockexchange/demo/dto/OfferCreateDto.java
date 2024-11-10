package com.stockexchange.demo.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfferCreateDto {
    private int quantity;
    private double pricePerShare;
    private Long stockId;
    private Long sellerId;
    private Long buyerId;
}