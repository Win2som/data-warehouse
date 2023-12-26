package com.example.clustereddatawarehouse;

import com.example.clustereddatawarehouse.entity.FXDeal;
import com.example.clustereddatawarehouse.exception.DealProcessingException;
import com.example.clustereddatawarehouse.model.DealRequest;
import com.example.clustereddatawarehouse.model.DealResponse;
import com.example.clustereddatawarehouse.repository.FXDealRepository;
import com.example.clustereddatawarehouse.service.DealService;
import com.example.clustereddatawarehouse.service.DealValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DealServiceTest {

    @Mock
    private FXDealRepository dealRepository;

    @Mock
    private DealValidationService validationService;

    @InjectMocks
    private DealService dealService;

    @Test
    void testProcessDeal_Success() {
        DealRequest dealRequest = new DealRequest("123", "USD", "EUR", BigDecimal.TEN);
        FXDeal fxDeal = FXDeal.builder().dealUniqueId("123").build();

        doNothing().when(validationService).validateDeal(ArgumentMatchers.any());
        when(dealRepository.existsByDealUniqueId("123")).thenReturn(false);

        DealResponse response = dealService.processDeal(dealRequest);

        assertEquals("SUCCESS", response.getStatus());
        assertEquals("Deal saved successfully", response.getMessage());

        verify(dealRepository, times(1)).save(any());
    }

    @Test
    void testProcessDeal_ValidationException() {
        DealRequest dealRequest = new DealRequest("123", "USD", "EUR", BigDecimal.TEN);

        doThrow(new DealProcessingException("Validation error")).when(validationService).validateDeal(ArgumentMatchers.any());

        DealResponse response = dealService.processDeal(dealRequest);

        assertEquals("ERROR", response.getStatus());
        assertEquals("Validation error", response.getMessage());

        verify(dealRepository, never()).save(any());
    }

    @Test
    void testProcessDeal_DealAlreadyExists() {
        DealRequest dealRequest = new DealRequest("123", "USD", "EUR", BigDecimal.TEN);

        doNothing().when(validationService).validateDeal(ArgumentMatchers.any());
        when(dealRepository.existsByDealUniqueId("123")).thenReturn(true);

        dealService.processDeal(dealRequest);

        verify(dealRepository, never()).save(any());
    }

}
