package com.stockexchange.demo.dto;

import com.stockexchange.demo.entity.Buyer;
import com.stockexchange.demo.entity.Seller;
import com.stockexchange.demo.entity.Stock;
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
