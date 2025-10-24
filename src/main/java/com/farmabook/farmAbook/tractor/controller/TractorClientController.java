package com.farmabook.farmAbook.tractor.controller;

import com.farmabook.farmAbook.tractor.dto.TractorClientDTO;
import com.farmabook.farmAbook.tractor.service.TractorClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tractor-clients")
public class TractorClientController {

    private final TractorClientService clientService;

    public TractorClientController(TractorClientService clientService) {
        this.clientService = clientService;
    }

    // Add a client for a farmer
    @PostMapping
    public ResponseEntity<TractorClientDTO> addClient(@RequestBody TractorClientDTO dto) {
        return ResponseEntity.ok(clientService.addClient(dto));
    }

    // Get all clients of a farmer
    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<TractorClientDTO>> getClients(@PathVariable Long farmerId) {
        return ResponseEntity.ok(clientService.getClientsByFarmer(farmerId));
    }

    // Delete a client
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
