package com.example.clustereddatawarehouse.controller;

import com.example.clustereddatawarehouse.exception.ValidationException;
import com.example.clustereddatawarehouse.model.DealRequest;
import com.example.clustereddatawarehouse.model.DealResponse;
import com.example.clustereddatawarehouse.service.DealService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

@RestController
@Slf4j
@RequestMapping("/api/deals")
public class DealController {
        @Autowired
        private DealService dealService;

        @PostMapping("/json")
        public ResponseEntity<DealResponse> processJsonDeal(@RequestBody @Valid DealRequest dealRequest) {
            try {
                DealResponse response = dealService.processDeal(dealRequest);
                return ResponseEntity.status(getResponseStatus(response)).body(response);
            } catch (ValidationException ex){
                log.error("Validation error for field '{}': {}", ex.getFieldName(), ex.getMessage());
                DealResponse errorResponse = DealResponse.builder()
                        .status("ERROR")
                        .message("Validation Error").build();
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
        }


        @PostMapping(value = "/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<DealResponse> processCsvDeal(@RequestParam("file") MultipartFile file) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] dealData = line.split(",");
                    DealRequest dealRequest = new DealRequest();
                    dealRequest.setDealUniqueId(dealData[0]);
                    dealRequest.setFromCurrencyISOCode(dealData[1]);
                    dealRequest.setToCurrencyISOCode(dealData[2]);
                    dealRequest.setDealAmount(new BigDecimal(dealData[3]));

                    DealResponse response = dealService.processDeal(dealRequest);
                    if (!"SUCCESS".equals(response.getStatus())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    }
                }
                return ResponseEntity.status(HttpStatus.CREATED).body(new DealResponse("SUCCESS", "Deals saved successfully"));
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DealResponse("ERROR", "Error processing CSV file: " + e.getMessage()));
            }
        }


        private HttpStatus getResponseStatus(DealResponse response) {
            return "SUCCESS".equals(response.getStatus()) ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        }

}
