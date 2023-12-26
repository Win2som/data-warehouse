package com.example.clustereddatawarehouse.service;

import com.example.clustereddatawarehouse.entity.FXDeal;
import com.example.clustereddatawarehouse.exception.DealProcessingException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Objects;

@Service
public class DealValidationService {

    public void validateDeal(FXDeal deal) {
        if (Objects.isNull(deal)) {
            throw new DealProcessingException("Deal cannot be null");
        }

        validateDealUniqueId(deal.getDealUniqueId());
        validateCurrencyISOCode(deal.getFromCurrencyISOCode(), "From Currency");
        validateCurrencyISOCode(deal.getToCurrencyISOCode(), "To Currency");
        validateDealAmount(deal.getDealAmount());
    }

    private void validateDealUniqueId(String dealUniqueId) {
        if (Objects.isNull(dealUniqueId) || dealUniqueId.trim().isEmpty()) {
            throw new DealProcessingException("Deal Id is not unique");
        }
    }

    private void validateCurrencyISOCode(String currencyISOCode, String field) {
        if (Objects.isNull(currencyISOCode) || currencyISOCode.trim().isEmpty()) {
            throw new DealProcessingException(field + " ISO Code is required");
        }
        try {
            Currency.getInstance(currencyISOCode);
        } catch (IllegalArgumentException e) {
            throw new DealProcessingException(currencyISOCode + " is not a valid ISO currency code");
        }
    }


    private void validateDealAmount(BigDecimal dealAmount) {
        if (Objects.isNull(dealAmount)) {
            throw new DealProcessingException("Deal Amount is required");
        }
        if (dealAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DealProcessingException("Deal Amount must be greater than 0");
        }
    }
}

