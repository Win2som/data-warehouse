package com.example.clustereddatawarehouse;

import com.example.clustereddatawarehouse.entity.FXDeal;
import com.example.clustereddatawarehouse.exception.DealProcessingException;
import com.example.clustereddatawarehouse.service.DealValidationService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DealValidationServiceTest {

    private final DealValidationService validationService = new DealValidationService();

    @Test
    void testValidateDeal_ValidDeal() {
        FXDeal validDeal = createValidDeal();

        assertDoesNotThrow(() -> validationService.validateDeal(validDeal));
    }

    @Test
    void testValidateDeal_NullDeal() {
        assertThrows(DealProcessingException.class, () -> validationService.validateDeal(null));
    }

    @Test
    void testValidateDeal_NullDealUniqueId() {
        FXDeal dealWithNullId = createValidDeal();
        dealWithNullId.setDealUniqueId(null);

        assertThrows(DealProcessingException.class, () -> validationService.validateDeal(dealWithNullId));
    }

    @Test
    void testValidateDeal_EmptyDealUniqueId() {
        FXDeal dealWithEmptyId = createValidDeal();
        dealWithEmptyId.setDealUniqueId("");

        assertThrows(DealProcessingException.class, () -> validationService.validateDeal(dealWithEmptyId));
    }

    @Test
    void testValidateDeal_InvalidFromCurrencyISOCode() {
        FXDeal dealWithInvalidFromCurrency = createValidDeal();
        dealWithInvalidFromCurrency.setFromCurrencyISOCode("INVALID");

        assertThrows(DealProcessingException.class, () -> validationService.validateDeal(dealWithInvalidFromCurrency));
    }

    @Test
    void testValidateDeal_EmptyCurrencyISOCode() {
        FXDeal dealWithEmptyCurrency = createValidDeal();
        dealWithEmptyCurrency.setFromCurrencyISOCode("");

        assertThrows(DealProcessingException.class, () -> validationService.validateDeal(dealWithEmptyCurrency));
    }

    @Test
    void testValidateDeal_NullDealAmount() {
        FXDeal dealWithNullAmount = createValidDeal();
        dealWithNullAmount.setDealAmount(null);

        assertThrows(DealProcessingException.class, () -> validationService.validateDeal(dealWithNullAmount));
    }

    @Test
    void testValidateDeal_NegativeDealAmount() {
        FXDeal dealWithNegativeAmount = createValidDeal();
        dealWithNegativeAmount.setDealAmount(BigDecimal.valueOf(-10));

        assertThrows(DealProcessingException.class, () -> validationService.validateDeal(dealWithNegativeAmount));
    }

    private FXDeal createValidDeal() {
        return FXDeal.builder()
                .dealUniqueId("123")
                .fromCurrencyISOCode("USD")
                .toCurrencyISOCode("EUR")
                .dealAmount(BigDecimal.TEN)
                .build();
    }
}

