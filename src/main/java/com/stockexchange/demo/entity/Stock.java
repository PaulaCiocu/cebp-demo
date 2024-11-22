package com.stockexchange.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    private Integer totalShares;
    private Integer remainingShares;

    @PrePersist
    public void initializeRemainingShares() {
        if (this.remainingShares == null) {
            this.remainingShares = this.totalShares;
        }
    }
}
