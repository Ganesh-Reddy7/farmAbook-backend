package com.farmabook.farmAbook.loan.controller;

import com.farmabook.farmAbook.dto.ApiResponse;
import com.farmabook.farmAbook.loan.dto.InterestCalculationHistoryDTO;
import com.farmabook.farmAbook.loan.dto.InterestCalculatorRequest;
import com.farmabook.farmAbook.loan.dto.InterestCalculatorResponse;
import com.farmabook.farmAbook.loan.service.InterestCalculatorService;
import com.farmabook.farmAbook.util.ApiResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interest")
public class InterestCalculatorController {

    @Autowired
    private InterestCalculatorService service;

    // -------- SIMPLE INTEREST --------
    @PostMapping("/simple")
    public ResponseEntity<ApiResponse<InterestCalculatorResponse>> calculateSimple(
            @RequestBody InterestCalculatorRequest request
    ) {
        InterestCalculatorResponse response = service.calculateSimpleInterest(request);
        return ApiResponseUtil.success(
                "Simple interest calculated successfully",
                response
        );
    }

    @PostMapping("/compound")
    public ResponseEntity<ApiResponse<InterestCalculatorResponse>> calculateCompound(
            @RequestBody InterestCalculatorRequest request
    ) {

        InterestCalculatorResponse response = service.calculateCompoundInterest(request);
        return ApiResponseUtil.success(
                "Compound interest calculated successfully",
                response
        );
    }
    @GetMapping("/farmer/{farmerId}/type/{type}")
    public ResponseEntity<ApiResponse<List<InterestCalculationHistoryDTO>>> getHistoryByFarmerAndType(
            @PathVariable Long farmerId,
            @PathVariable String type
    ) {
        List<InterestCalculationHistoryDTO> history = service.getHistoryByFarmerAndType(farmerId, type);

        return ApiResponseUtil.success(
                "Interest calculation history fetched successfully",
                history
        );
    }
    @GetMapping("/histroy/{farmerId}")
    public ResponseEntity<ApiResponse<List<InterestCalculationHistoryDTO>>> getHistoryByFarmer(@PathVariable Long farmerId) {
        List<InterestCalculationHistoryDTO> history = service.getHistoryByFarmerId(farmerId);

        return ApiResponseUtil.success(
                "Interest calculation history fetched successfully",
                history
        );
    }
    @DeleteMapping("/deleteHistory/{historyId}")
    public ResponseEntity<ApiResponse<Void>> deleteHistory(
            @PathVariable Long historyId
    ) {
        service.deleteHistoryById(historyId);
        return ApiResponseUtil.success(
                "Interest calculation history deleted successfully",
                null
        );
    }
    @DeleteMapping("/deleteHistory/farmer/{farmerId}/type/{type}")
    public ResponseEntity<ApiResponse<Void>> deleteHistoryByFarmerAndType(
            @PathVariable Long farmerId,
            @PathVariable String type
    ) {
        service.deleteHistoryByFarmerAndType(farmerId, type);
        return ApiResponseUtil.success(
                "Interest calculation history deleted successfully",
                null
        );
    }

    @DeleteMapping("/deleteHistory/farmer/{farmerId}")
    public ResponseEntity<ApiResponse<Void>> deleteByFarmerId(
            @PathVariable Long farmerId
    ) {
        service.deleteByFarmerId(farmerId);
        return ApiResponseUtil.success(
                "Interest calculation history deleted successfully",
                null
        );
    }
}
