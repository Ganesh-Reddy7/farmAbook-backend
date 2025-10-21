package com.farmabook.farmAbook.tractor.service;

import com.farmabook.farmAbook.entity.Farmer;
import com.farmabook.farmAbook.repository.FarmerRepository;
import com.farmabook.farmAbook.tractor.dto.TractorActivityDTO;
import com.farmabook.farmAbook.tractor.entity.Tractor;
import com.farmabook.farmAbook.tractor.entity.TractorActivity;
import com.farmabook.farmAbook.tractor.enums.PaymentStatus;
import com.farmabook.farmAbook.tractor.repository.TractorActivityRepository;
import com.farmabook.farmAbook.tractor.repository.TractorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TractorActivityService {

    private final TractorActivityRepository activityRepository;
    private final TractorRepository tractorRepository;
    private final FarmerRepository farmerRepository;

    public TractorActivityService(
            TractorActivityRepository activityRepository,
            TractorRepository tractorRepository,
            FarmerRepository farmerRepository
    ) {
        this.activityRepository = activityRepository;
        this.tractorRepository = tractorRepository;
        this.farmerRepository = farmerRepository;
    }

    // ✅ Create a new tractor activity
    public TractorActivity createActivity(TractorActivityDTO dto) {
        Tractor tractor = tractorRepository.findById(dto.getTractorId())
                .orElseThrow(() -> new EntityNotFoundException("Tractor not found with id " + dto.getTractorId()));

        Farmer farmer = farmerRepository.findById(dto.getFarmerId())
                .orElseThrow(() -> new EntityNotFoundException("Farmer not found with id " + dto.getFarmerId()));

        TractorActivity activity = new TractorActivity();
        activity.setTractor(tractor);
        activity.setFarmer(farmer);
        activity.setActivityDate(dto.getActivityDate());
        activity.setStartTime(dto.getStartTime());
        activity.setEndTime(dto.getEndTime());
        activity.setClientName(dto.getClientName());
        activity.setAcresWorked(dto.getAcresWorked());
        activity.setAmountEarned(dto.getAmountEarned());
        activity.setNotes(dto.getNotes());

        double paid = dto.getAmountPaid() != null ? dto.getAmountPaid() : 0.0;
        activity.setAmountPaid(paid);

        double remaining = Math.max(activity.getAmountEarned() - paid, 0.0);
        activity.setAmountRemaining(remaining);

        if (paid == 0) {
            activity.setPaymentStatus(PaymentStatus.PENDING);
        } else if (remaining > 0) {
            activity.setPaymentStatus(PaymentStatus.PARTIALLY_PAID);
        } else {
            activity.setPaymentStatus(PaymentStatus.PAID);
        }

        return activityRepository.save(activity);
    }

    // ✅ Add a payment (partial or full)
    public TractorActivity addPayment(Long activityId, Double paymentAmount) {
        TractorActivity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found with id " + activityId));

        double newPaid = activity.getAmountPaid() + paymentAmount;
        double remaining = Math.max(activity.getAmountEarned() - newPaid, 0.0);

        activity.setAmountPaid(newPaid);
        activity.setAmountRemaining(remaining);

        if (newPaid == 0) {
            activity.setPaymentStatus(PaymentStatus.PENDING);
        } else if (remaining > 0) {
            activity.setPaymentStatus(PaymentStatus.PARTIALLY_PAID);
        } else {
            activity.setPaymentStatus(PaymentStatus.PAID);
        }

        return activityRepository.save(activity);
    }

    // ✅ Delete activity
    public void deleteActivity(Long id) {
        if (!activityRepository.existsById(id)) {
            throw new EntityNotFoundException("Activity not found with id " + id);
        }
        activityRepository.deleteById(id);
    }

    // ✅ Get activities by tractor
    public List<TractorActivityDTO> getActivitiesByTractor(Long tractorId) {
        return activityRepository.findByTractorId(tractorId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Get activities by farmer
    public List<TractorActivityDTO> getActivitiesByFarmer(Long farmerId) {
        return activityRepository.findByFarmerId(farmerId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Convert Entity → DTO
    public TractorActivityDTO toDTO(TractorActivity activity) {
        TractorActivityDTO dto = new TractorActivityDTO();
        dto.setId(activity.getId());
        dto.setTractorId(activity.getTractor().getId());
        dto.setFarmerId(activity.getFarmer().getId());
        dto.setActivityDate(activity.getActivityDate());
        dto.setStartTime(activity.getStartTime());
        dto.setEndTime(activity.getEndTime());
        dto.setClientName(activity.getClientName());
        dto.setAcresWorked(activity.getAcresWorked());
        dto.setAmountEarned(activity.getAmountEarned());
        dto.setAmountPaid(activity.getAmountPaid());
        dto.setAmountRemaining(activity.getAmountRemaining());
        dto.setPaymentStatus(activity.getPaymentStatus().name());
        dto.setNotes(activity.getNotes());
        return dto;
    }
}
