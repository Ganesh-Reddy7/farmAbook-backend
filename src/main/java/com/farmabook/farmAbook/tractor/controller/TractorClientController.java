package com.farmabook.farmAbook.tractor.controller;

import com.farmabook.farmAbook.tractor.dto.TractorClientDTO;
import com.farmabook.farmAbook.tractor.dto.TractorActivityDTO;
import com.farmabook.farmAbook.tractor.dto.ClientActivitySummaryDTO;
import com.farmabook.farmAbook.tractor.service.TractorClientService;
import com.farmabook.farmAbook.tractor.service.TractorActivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tractor-clients")
public class TractorClientController {

    private final TractorClientService clientService;
    private final TractorActivityService activityService;

    public TractorClientController(TractorClientService clientService,
                                   TractorActivityService activityService) {
        this.clientService = clientService;
        this.activityService = activityService;
    }

    @PostMapping
    public ResponseEntity<TractorClientDTO> addClient(@RequestBody TractorClientDTO dto) {
        return ResponseEntity.ok(clientService.addClient(dto));
    }

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<TractorClientDTO>> getClients(@PathVariable Long farmerId) {
        return ResponseEntity.ok(clientService.getClientsByFarmer(farmerId));
    }

    @GetMapping("/client/{clientId}/activities")
    public ResponseEntity<ClientActivitySummaryDTO> getActivitiesByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(activityService.getActivitiesByClient(clientId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
