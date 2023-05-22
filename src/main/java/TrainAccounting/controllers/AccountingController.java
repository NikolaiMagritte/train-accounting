package TrainAccounting.controllers;

import TrainAccounting.model.CarriagePassport;
import TrainAccounting.model.Station;
import TrainAccounting.model.StationPath;
import TrainAccounting.repositories.StationPathRepository;
import TrainAccounting.repositories.StationRepository;
import TrainAccounting.services.AcceptCarriagesService;
import TrainAccounting.services.DepartCarriagesService;
import TrainAccounting.services.RearrangeCarriagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounting")
public class AccountingController {
    private final AcceptCarriagesService acceptCarriagesService;
    private final RearrangeCarriagesService rearrangeCarriagesService;
    private final DepartCarriagesService departCarriagesService;
    private final StationRepository stationRepository;
    private final StationPathRepository stationPathRepository;

    @PostMapping("/accept")
    public ResponseEntity<String> acceptCarriages(@RequestBody List<CarriagePassport> carriages,
                                                  @RequestParam Long stationId,
                                                  @RequestParam Long stationPathId) {
        try {
            Station station = stationRepository.findById(stationId)
                    .orElseThrow(() -> new IllegalArgumentException("Station not found"));
            StationPath stationPath = stationPathRepository.findById(stationPathId)
                    .orElseThrow(() -> new IllegalArgumentException("Station path not found"));

            acceptCarriagesService.acceptCarriages(carriages, station, stationPath);

            return ResponseEntity.ok("Carriages accepted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/rearrange")
    public ResponseEntity<String> rearrangeCarriages(@RequestBody List<CarriagePassport> carriages,
                                                     @RequestParam Long stationId,
                                                     @RequestParam Long sourcePathId,
                                                     @RequestParam Long destinationPathId) {
        try {
            Station station = stationRepository.findById(stationId)
                    .orElseThrow(() -> new IllegalArgumentException("Station not found"));
            StationPath sourcePath = stationPathRepository.findById(sourcePathId)
                    .orElseThrow(() -> new IllegalArgumentException("Source station path not found"));
            StationPath destinationPath = stationPathRepository.findById(destinationPathId)
                    .orElseThrow(() -> new IllegalArgumentException("Destination station path not found"));

            rearrangeCarriagesService.rearrangeCarriages(carriages, station, sourcePath, destinationPath);

            return ResponseEntity.ok("Carriages rearranged successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/depart")
    public ResponseEntity<String> departCarriages(@RequestBody List<CarriagePassport> carriages,
                                                  @RequestParam Long stationId,
                                                  @RequestParam Long stationPathId) {
        try {
            Station station = stationRepository.findById(stationId)
                    .orElseThrow(() -> new IllegalArgumentException("Station not found"));
            StationPath stationPath = stationPathRepository.findById(stationPathId)
                    .orElseThrow(() -> new IllegalArgumentException("Station path not found"));

            departCarriagesService.departCarriages(carriages, station, stationPath);

            return ResponseEntity.ok("Carriages departed successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

