package com.example.clustereddatawarehouse.service;

import com.example.clustereddatawarehouse.entity.FXDeal;
import com.example.clustereddatawarehouse.exception.DealProcessingException;
import com.example.clustereddatawarehouse.model.DealRequest;
import com.example.clustereddatawarehouse.model.DealResponse;
import com.example.clustereddatawarehouse.repository.FXDealRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class DealService {

    private static final ModelMapper modelMapper = new ModelMapper();
    private static final Logger logger = LoggerFactory.getLogger(DealService.class);

    @Autowired
    private FXDealRepository dealRepository;

    @Autowired
    private DealValidationService validationService;

    public DealResponse processDeal(DealRequest dealRequest) {
        try {
            FXDeal deal = mapRequestToEntity(dealRequest);
            validationService.validateDeal(deal);
            saveDealToDatabase(deal);
            return createSuccessResponse("Deal saved successfully");
        } catch (DealProcessingException e) {
            logger.error("Validation error: {}", e.getMessage());
            return createErrorResponse(e.getMessage());
        }

    }

    private FXDeal mapRequestToEntity(DealRequest dealRequest) {
//       return modelMapper.map(dealRequest, FXDeal.class);
        return FXDeal.builder()
                .dealUniqueId(dealRequest.getDealUniqueId())
                .fromCurrencyISOCode(dealRequest.getFromCurrencyISOCode())
                .toCurrencyISOCode(dealRequest.getToCurrencyISOCode())
                .dealAmount(dealRequest.getDealAmount())
                .build();

    }

    private void saveDealToDatabase(FXDeal deal) {
        if (!dealRepository.existsByDealUniqueId(deal.getDealUniqueId())) {
            logger.info("New fx deal saved to the database: {}"+ deal.getDealUniqueId());
            dealRepository.save(deal);
        } else {
            throw new DealProcessingException("Deal with ID " + deal.getDealUniqueId() + " already exists in the database. Skipping.");
        }
    }

    private DealResponse createSuccessResponse(String message) {
        DealResponse response = new DealResponse();
        response.setStatus("SUCCESS");
        response.setMessage(message);
        return response;
    }

    private DealResponse createErrorResponse(String message) {
        DealResponse response = new DealResponse();
        response.setStatus("ERROR");
        response.setMessage(message);
        return response;
    }
}

