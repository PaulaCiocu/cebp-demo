package com.stockexchange.demo.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestUpdateDto {
        private Integer quantity;
        private Double maxPricePerShare;
        private Long stockId;
        private Long buyerId;
}
