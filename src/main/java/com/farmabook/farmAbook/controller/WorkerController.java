package com.farmabook.farmAbook.controller;

import com.farmabook.farmAbook.dto.WorkerDTO;
import com.farmabook.farmAbook.service.WorkerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workers")
public class WorkerController {

    private final WorkerService workerService;

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @PostMapping
    public ResponseEntity<WorkerDTO> createWorker(@Valid @RequestBody WorkerDTO workerDTO) {
        WorkerDTO created = workerService.createWorker(workerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<WorkerDTO>> getAllWorkers() {
        return ResponseEntity.ok(workerService.getAllWorkers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkerDTO> getWorkerById(@PathVariable Long id) {
        return ResponseEntity.ok(workerService.getWorkerById(id));
    }

    @GetMapping("/investment/{investmentId}")
    public ResponseEntity<List<WorkerDTO>> getWorkersByInvestment(@PathVariable Long investmentId) {
        return ResponseEntity.ok(workerService.getWorkersByInvestment(investmentId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorker(@PathVariable Long id) {
        workerService.deleteWorker(id);
        return ResponseEntity.noContent().build();
    }
}
