package com.stockexchange.demo.dto.Stock;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockCreateDto {
    private String companyName;
    private Integer totalShares;
}
