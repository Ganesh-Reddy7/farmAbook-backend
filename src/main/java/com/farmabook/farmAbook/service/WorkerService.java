package com.farmabook.farmAbook.service;

import com.farmabook.farmAbook.dto.WorkerDTO;
import com.farmabook.farmAbook.entity.Investment;
import com.farmabook.farmAbook.entity.Worker;
import com.farmabook.farmAbook.exception.ResourceNotFoundException;
import com.farmabook.farmAbook.repository.InvestmentRepository;
import com.farmabook.farmAbook.repository.WorkerRepository;
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

    public WorkerDTO createWorkerByInvestment(WorkerDTO dto) {
        Investment investment = investmentRepository.findById(dto.getInvestmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Investment not found with id " + dto.getInvestmentId()));

        Worker worker = new Worker();
        worker.setName(dto.getName());
        worker.setWage(dto.getWage());
        worker.setRole(dto.getRole());
        worker.setInvestment(investment);

        Worker saved = workerRepository.save(worker);
        dto.setId(saved.getId());
        return dto;
    }

    public List<WorkerDTO> getWorkersByInvestment(Long investmentId) {
        return workerRepository.findByInvestmentId(investmentId)
                .stream()
                .map(w -> {
                    WorkerDTO dto = new WorkerDTO();
                    dto.setId(w.getId());
                    dto.setName(w.getName());
                    dto.setWage(w.getWage());
                    dto.setRole(w.getRole());
                    dto.setInvestmentId(w.getInvestment().getId());
                    return dto;
                }).collect(Collectors.toList());
    }
    public WorkerDTO createWorker(WorkerDTO dto) {
        Worker worker = new Worker();
        worker.setName(dto.getName());
        worker.setRole(dto.getRole());
        worker.setWage(dto.getWage());

        workerRepository.save(worker);
        dto.setId(worker.getId());
        return dto;
    }

    // Get all
    public List<WorkerDTO> getAllWorkers() {
        return workerRepository.findAll().stream().map(worker -> {
            WorkerDTO dto = new WorkerDTO();
            dto.setId(worker.getId());
            dto.setName(worker.getName());
            dto.setRole(worker.getRole());
            dto.setWage(worker.getWage());
            return dto;
        }).collect(Collectors.toList());
    }

    // Get by ID
    public WorkerDTO getWorkerById(Long id) {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found"));

        WorkerDTO dto = new WorkerDTO();
        dto.setId(worker.getId());
        dto.setName(worker.getName());
        dto.setRole(worker.getRole());
        dto.setWage(worker.getWage());
        return dto;
    }

    // Delete
    public void deleteWorker(Long id) {
        if (!workerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Worker not found");
        }
        workerRepository.deleteById(id);
    }
}
