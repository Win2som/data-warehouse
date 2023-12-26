package com.example.clustereddatawarehouse;

import com.example.clustereddatawarehouse.controller.DealController;
import com.example.clustereddatawarehouse.exception.ValidationException;
import com.example.clustereddatawarehouse.model.DealRequest;
import com.example.clustereddatawarehouse.model.DealResponse;
import com.example.clustereddatawarehouse.service.DealService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealControllerTest {

    @Mock
    private DealService dealService;

    @InjectMocks
    private DealController dealController;

    @Test
    void testProcessJsonDeal_Success() {
        // Arrange
        DealRequest dealRequest = createValidDealRequest();
        DealResponse successResponse = createSuccessResponse();

        when(dealService.processDeal(dealRequest)).thenReturn(successResponse);

        // Act
        ResponseEntity<DealResponse> responseEntity = dealController.processJsonDeal(dealRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertSame(successResponse, responseEntity.getBody());
    }

    @Test
    void testProcessJsonDeal_ValidationFailure() {
        // Arrange
        DealRequest dealRequest = createValidDealRequest();
        ValidationException validationException = new ValidationException("Field", "Validation error");

        when(dealService.processDeal(dealRequest)).thenThrow(validationException);

        // Act
        ResponseEntity<DealResponse> responseEntity = dealController.processJsonDeal(dealRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("ERROR", responseEntity.getBody().getStatus());
        assertEquals("Validation Error", responseEntity.getBody().getMessage());
    }

    @Test
    void testProcessCsvDeal_Success() throws IOException {
        MultipartFile file = createCsvMultipartFile("123,USD,EUR,10.0");

        when(dealService.processDeal(any())).thenReturn(createSuccessResponse());

        ResponseEntity<DealResponse> responseEntity = dealController.processCsvDeal(file);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void testProcessCsvDeal_ErrorReadingFile() throws IOException {
        // Arrange
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenThrow(new IOException("Error reading file"));

        // Act
        ResponseEntity<DealResponse> responseEntity = dealController.processCsvDeal(file);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("ERROR", responseEntity.getBody().getStatus());
        assertTrue(responseEntity.getBody().getMessage().contains("Error processing CSV file"));
    }
    @Test
    void testProcessCsvDeal_ValidationFailure() throws IOException {
        // Arrange
        String csvData = "123,USD,EUR,0";
        MultipartFile file = createCsvMultipartFile(csvData);

        // Mock the dealService
        when(dealService.processDeal(any())).thenReturn(createNonSuccessResponse());

        // Act
        ResponseEntity<DealResponse> responseEntity = dealController.processCsvDeal(file);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("ERROR", responseEntity.getBody().getStatus());
    }

    private DealRequest createValidDealRequest() {
        return new DealRequest("123", "USD", "EUR", BigDecimal.TEN);
    }

    private DealResponse createSuccessResponse() {
        return DealResponse.builder()
                .status("SUCCESS")
                .message("Deal saved successfully")
                .build();
    }
    private DealResponse createNonSuccessResponse() {
        return DealResponse.builder()
                .status("ERROR")
                .message("Validation Error")
                .build();
    }
    private MultipartFile createCsvMultipartFile(String content) throws IOException {
        return new MockMultipartFile("file", "test.csv", "text/csv", content.getBytes());
    }
}

