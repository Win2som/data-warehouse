package com.example.clustereddatawarehouse.model;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealRequest {

    @NotBlank(message = "Deal Unique Id is required")
    private String dealUniqueId;

    @NotBlank(message = "From Currency ISO Code is required")
    @Size(max = 3, message = "Should not be more than 3 characters")
    private String fromCurrencyISOCode;

    @NotBlank(message = "To Currency ISO Code is required")
    @Size(max = 3, message = "Should not be more than 3 characters")
    private String toCurrencyISOCode;

    @NotNull(message = "Deal Amount is required")
//    @DecimalMin(value = "0.0", inclusive = false, message = "Deal Amount must be greater than 0")
    private BigDecimal dealAmount;

}
