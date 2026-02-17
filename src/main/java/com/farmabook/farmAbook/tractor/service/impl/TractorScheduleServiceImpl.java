package com.farmabook.farmAbook.tractor.service.impl;
import com.farmabook.farmAbook.tractor.entity.TractorClient;
import com.farmabook.farmAbook.tractor.enums.ClientType;
import com.farmabook.farmAbook.tractor.repository.TractorClientRepository;
import com.farmabook.farmAbook.tractor.service.DieselRateService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.farmabook.farmAbook.tractor.entity.TractorSchedule;
import com.farmabook.farmAbook.tractor.repository.TractorScheduleRepository;
import com.farmabook.farmAbook.tractor.service.TractorScheduleService;
import com.farmabook.farmAbook.tractor.dto.*;
import com.farmabook.farmAbook.tractor.enums.TractorScheduleStatus;

@Service
@RequiredArgsConstructor
public class TractorScheduleServiceImpl implements TractorScheduleService {

    private final TractorScheduleRepository repository;
    private  final TractorClientRepository clientRepository;
    private final DieselRateService dieselRateService;
    private static final BigDecimal FUEL_PER_ACRE = BigDecimal.valueOf(3);

    @Override
    public void createSchedule(CreateScheduleRequest request) {

        BigDecimal acres = BigDecimal.valueOf(
                request.getAreaUnit().toAcres(request.getArea().doubleValue())
        );

        BigDecimal expectedJobCost = acres.multiply(request.getRatePerAcre());
        BigDecimal expectedFuelLitres = acres.multiply(FUEL_PER_ACRE);
        BigDecimal dieselRate = dieselRateService.getDieselRatePerLitre();
        BigDecimal expectedFuelCost = expectedFuelLitres.multiply(dieselRate);

        TractorClient client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        if (client.getClientType() == ClientType.OTHERS) {
            if (request.getClientDisplayName() == null ||
                    request.getClientDisplayName().isBlank()) {
                throw new IllegalArgumentException(
                        "Client name is required for OTHERS client type"
                );
            }
        }

        TractorSchedule schedule = new TractorSchedule();
        schedule.setTractorId(request.getTractorId());
        schedule.setFarmerId(request.getFarmerId());
        schedule.setClientId(client.getId());

        if (client.getClientType() == ClientType.OTHERS) {
            schedule.setClientDisplayName(request.getClientDisplayName());
        } else {
            schedule.setClientDisplayName(client.getName());
        }

        schedule.setFieldId(request.getFieldId());
        schedule.setJobType(request.getJobType());
        schedule.setScheduleDateTime(request.getScheduleDateTime());

        schedule.setArea(request.getArea());
        schedule.setAreaUnit(request.getAreaUnit());
        schedule.setAreaInAcres(acres);

        schedule.setExpectedJobCost(expectedJobCost);
        schedule.setExpectedFuelLitres(expectedFuelLitres);
        schedule.setExpectedFuelCost(expectedFuelCost);

        schedule.setPriorityOrder(request.getPriorityOrder());
        schedule.setNotes(request.getNotes());

        repository.save(schedule);
    }



    @Override
    public List<TractorScheduleResponse> getSchedulesByFarmer(Long farmerId) {
        return repository.findByFarmerIdOrderByScheduleDateTimeAsc(farmerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TractorScheduleResponse> getSchedulesByFarmer(Long farmerId, TractorScheduleStatus status) {
        List<TractorSchedule> schedules;
        if (status != null) {
            schedules = repository.findByFarmerIdAndStatusOrderByScheduleDateTimeAsc(farmerId, status);
        } else {
            schedules = repository.findByFarmerIdOrderByScheduleDateTimeAsc(farmerId);
        }
        return schedules.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TractorScheduleResponse updateScheduleStatus(Long scheduleId, TractorScheduleStatus status) {

        TractorSchedule schedule = repository.findById(scheduleId).orElseThrow(() -> new EntityNotFoundException("Schedule not found"));

        validateStatusTransition(schedule.getStatus(), status);

        schedule.setStatus(status);
        schedule.setUpdatedAt(LocalDateTime.now());
        TractorSchedule saved = repository.save(schedule);

        return mapToResponse(saved);
    }

    private void validateStatusTransition(
            TractorScheduleStatus current,
            TractorScheduleStatus next
    ) {
        switch (current) {
            case PENDING -> {
                if (next != TractorScheduleStatus.IN_PROGRESS &&
                        next != TractorScheduleStatus.CANCELLED) {
                    throw new IllegalStateException(
                            "Pending job can only go to IN_PROGRESS or CANCELLED"
                    );
                }
            }
            case IN_PROGRESS -> {
                if (next != TractorScheduleStatus.COMPLETED &&
                        next != TractorScheduleStatus.CANCELLED) {
                    throw new IllegalStateException(
                            "In-progress job can only go to COMPLETED or CANCELLED"
                    );
                }
            }
            case COMPLETED, CANCELLED -> {
                throw new IllegalStateException(
                        "Cannot change status of a job that is " + current
                );
            }
        }
    }



    private TractorScheduleResponse mapToResponse(TractorSchedule s) {

        BigDecimal dieselRate = dieselRateService.getDieselRatePerLitre();

        BigDecimal expectedFuelCost = s.getExpectedFuelLitres()
                .multiply(dieselRate);

        return TractorScheduleResponse.builder()
                .id(s.getId())
                .tractorId(s.getTractorId())
                .farmerId(s.getFarmerId())
                .clientId(s.getClientId())
                .clientDisplayName(s.getClientDisplayName())

                .jobType(s.getJobType())
                .scheduleDateTime(s.getScheduleDateTime())
                .status(s.getStatus())
                .priorityOrder(s.getPriorityOrder())

                .area(s.getArea())
                .areaUnit(s.getAreaUnit())
                .areaInAcres(s.getAreaInAcres())

                .expectedJobCost(s.getExpectedJobCost())
                .expectedFuelLitres(s.getExpectedFuelLitres())
                .expectedFuelCost(expectedFuelCost)
                .build();
    }


}
