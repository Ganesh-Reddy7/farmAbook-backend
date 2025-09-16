package com.farmabook.farmAbook.controller;

import com.farmabook.farmAbook.dto.ReportRequestDTO;
import com.farmabook.farmAbook.dto.FarmerReportDTO;
import com.farmabook.farmAbook.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // Flexible custom date range
    @PostMapping("/farmer")
    public ResponseEntity<FarmerReportDTO> generateFarmerReport(@RequestBody ReportRequestDTO request) {
        FarmerReportDTO report = reportService.generateFarmerReport(
                request.getFarmerId(),
                request.getStartDate(),
                request.getEndDate(),
                request.getPeriodLabel()
        );
        return ResponseEntity.ok(report);
    }

    // Predefined - Quarterly report
    @PostMapping("/farmer/quarterly")
    public ResponseEntity<FarmerReportDTO> generateQuarterlyReport(@RequestParam Long farmerId,
                                                                   @RequestParam int year,
                                                                   @RequestParam int quarter) {
        LocalDate startDate;
        LocalDate endDate;

        switch (quarter) {
            case 1:
                startDate = LocalDate.of(year, 1, 1);
                endDate = LocalDate.of(year, 3, 31);
                break;
            case 2:
                startDate = LocalDate.of(year, 4, 1);
                endDate = LocalDate.of(year, 6, 30);
                break;
            case 3:
                startDate = LocalDate.of(year, 7, 1);
                endDate = LocalDate.of(year, 9, 30);
                break;
            case 4:
                startDate = LocalDate.of(year, 10, 1);
                endDate = LocalDate.of(year, 12, 31);
                break;
            default:
                throw new IllegalArgumentException("Quarter must be between 1 and 4");
        }

        FarmerReportDTO report = reportService.generateFarmerReport(farmerId, startDate, endDate, "Q" + quarter + " " + year);
        return ResponseEntity.ok(report);
    }

    // Predefined - Yearly report
    @PostMapping("/farmer/yearly")
    public ResponseEntity<FarmerReportDTO> generateYearlyReport(@RequestParam Long farmerId,
                                                                @RequestParam int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        FarmerReportDTO report = reportService.generateFarmerReport(farmerId, startDate, endDate, "Year " + year);
        return ResponseEntity.ok(report);
    }

    // Predefined - Seasonal report (example: Summer, Winter, etc.)
    @PostMapping("/farmer/seasonal")
    public ResponseEntity<FarmerReportDTO> generateSeasonalReport(@RequestParam Long farmerId,
                                                                  @RequestParam int year,
                                                                  @RequestParam String season) {
        LocalDate startDate;
        LocalDate endDate;

        switch (season.toLowerCase()) {
            case "summer":
                startDate = LocalDate.of(year, 3, 1);
                endDate = LocalDate.of(year, 5, 31);
                break;
            case "monsoon":
                startDate = LocalDate.of(year, 6, 1);
                endDate = LocalDate.of(year, 8, 31);
                break;
            case "autumn":
                startDate = LocalDate.of(year, 9, 1);
                endDate = LocalDate.of(year, 11, 30);
                break;
            case "winter":
                startDate = LocalDate.of(year, 12, 1);
                endDate = LocalDate.of(year, 2, 28);
                // Handle leap year Feb 29
                if (year % 4 == 0) {
                    endDate = LocalDate.of(year, 2, 29);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid season. Use: summer, monsoon, autumn, winter");
        }

        FarmerReportDTO report = reportService.generateFarmerReport(farmerId, startDate, endDate,
                season.substring(0, 1).toUpperCase() + season.substring(1) + " " + year);
        return ResponseEntity.ok(report);
    }
}
