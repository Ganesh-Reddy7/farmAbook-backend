package com.farmabook.farmAbook.service;

import com.farmabook.farmAbook.dto.WorkerDTO;
import com.farmabook.farmAbook.entity.Worker;
import com.farmabook.farmAbook.entity.Investment;
import com.farmabook.farmAbook.exception.ResourceNotFoundException;
import com.farmabook.farmAbook.repository.WorkerRepository;
import com.farmabook.farmAbook.repository.InvestmentRepository;
import com.farmabook.farmAbook.repository.CropRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkerService {

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    private CropRepository cropRepository;

    public WorkerDTO createWorker(WorkerDTO dto) {
        Worker worker = new Worker();
        worker.setName(dto.getName());
        worker.setRole(dto.getRole());
        worker.setWage(dto.getWage());
        worker.setPaymentDone(false);

        if (dto.getInvestmentId() != null) {
            Investment inv = investmentRepository.findById(dto.getInvestmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Investment not found"));
            worker.setInvestment(inv);
        }

        workerRepository.save(worker);
        dto.setId(worker.getId());
        return dto;
    }

    public List<WorkerDTO> addWorkersToInvestment(Long investmentId, List<WorkerDTO> workerDTOs) {
        Investment investment = investmentRepository.findById(investmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Investment not found"));

        double existingAssigned = workerRepository.findByInvestmentId(investmentId)
                .stream().mapToDouble(Worker::getWage).sum();
        double totalNew = workerDTOs.stream().mapToDouble(WorkerDTO::getWage).sum();

        if (existingAssigned + totalNew > investment.getAmount()) {
            throw new RuntimeException("Total assigned wages exceed investment amount!");
        }

        List<Worker> workers = workerDTOs.stream().map(dto -> {
            Worker w = new Worker();
            w.setName(dto.getName());
            w.setRole(dto.getRole());
            w.setWage(dto.getWage());
            w.setPaymentDone(false);
            w.setInvestment(investment);
            return w;
        }).collect(Collectors.toList());

        List<Worker> saved = workerRepository.saveAll(workers);
        return saved.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<WorkerDTO> getAllWorkers() {
        return workerRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public WorkerDTO getWorkerById(Long id) {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found"));
        return mapToDTO(worker);
    }

    public List<WorkerDTO> getWorkersByInvestment(Long investmentId) {
        return workerRepository.findByInvestmentId(investmentId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public void deleteWorker(Long id) {
        if (!workerRepository.existsById(id))
            throw new ResourceNotFoundException("Worker not found");
        workerRepository.deleteById(id);
    }

    public WorkerDTO updatePaymentStatus(Long workerId, boolean paymentDone) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found"));
        worker.setPaymentDone(paymentDone);
        Worker saved = workerRepository.save(worker);

        if (worker.getInvestment() != null)
            updateInvestmentRemainingAmount(worker.getInvestment().getId());

        return mapToDTO(saved);
    }

    public List<WorkerDTO> updateWorkersPaymentStatus(Long investmentId, List<Long> workerIds, boolean paymentDone) {
        List<Worker> workers = workerRepository.findAllById(workerIds);
        workers.forEach(worker -> {
            if (!worker.getInvestment().getId().equals(investmentId))
                throw new RuntimeException("Worker " + worker.getId() + " does not belong to investment " + investmentId);
            worker.setPaymentDone(paymentDone);
        });

        List<Worker> saved = workerRepository.saveAll(workers);
        updateInvestmentRemainingAmount(investmentId);
        return saved.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private void updateInvestmentRemainingAmount(Long investmentId) {
        Investment investment = investmentRepository.findById(investmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Investment not found"));

        double remaining = workerRepository.findByInvestmentId(investmentId).stream()
                .filter(w -> !w.getPaymentDone())
                .mapToDouble(Worker::getWage)
                .sum();

        investment.setRemainingAmount(remaining);
        investmentRepository.save(investment);

        if (investment.getCrop() != null) {
            double cropRemaining = investment.getCrop().getInvestments().stream()
                    .mapToDouble(inv -> inv.getRemainingAmount() != null ? inv.getRemainingAmount() : 0.0)
                    .sum();
            investment.getCrop().setRemainingInvestment(cropRemaining);
            cropRepository.save(investment.getCrop());
        }
    }

    private WorkerDTO mapToDTO(Worker w) {
        WorkerDTO dto = new WorkerDTO();
        dto.setId(w.getId());
        dto.setName(w.getName());
        dto.setRole(w.getRole());
        dto.setWage(w.getWage());
        dto.setPaymentDone(w.isPaymentDone());
        dto.setInvestmentId(w.getInvestment() != null ? w.getInvestment().getId() : null);
        return dto;
    }
}
