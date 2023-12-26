package com.example.clustereddatawarehouse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FXDeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DEAL_UNIQUE_ID", nullable = false)
    private String dealUniqueId;

    @Column(nullable = false)
    private String fromCurrencyISOCode;

    @Column(nullable = false)
    private String toCurrencyISOCode;

    @Column(nullable = false)
//    @DecimalMin(value = "0.0", inclusive = false, message = "Deal Amount must be greater than 0")
    private BigDecimal dealAmount;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
