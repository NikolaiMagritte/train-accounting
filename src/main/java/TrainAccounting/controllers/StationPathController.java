package TrainAccounting.controllers;

import TrainAccounting.model.StationPath;
import TrainAccounting.repositories.StationPathRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/station-paths")
@RequiredArgsConstructor
public class StationPathController {
    private static final Logger logger = LoggerFactory.getLogger(StationPathController.class);

    private final StationPathRepository stationPathRepository;

    @GetMapping
    public ResponseEntity<List<StationPath>> getAllStationPaths() {
        List<StationPath> stationPaths = stationPathRepository.findAll();
        if (!stationPaths.isEmpty()) {
            logger.info("Retrieved all station paths");
            return ResponseEntity.ok().body(stationPaths);
        } else {
            logger.info("No station paths found");
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationPath> getStationPathById(@PathVariable Long id) {
        try {
            StationPath stationPath = stationPathRepository.findById(id)
                    .orElseThrow(() -> {
                        String errorMessage = "Station path not found";
                        logger.error(errorMessage);
                        return new IllegalArgumentException(errorMessage);
                    });
            logger.info("Retrieved station path with ID: {}", id);
            return ResponseEntity.ok(stationPath);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to retrieve station path with ID: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<StationPath> createStationPath(@RequestBody StationPath stationPath) {
        try {
            StationPath createdStationPath = stationPathRepository.save(stationPath);
            logger.info("Created station path with ID: {}", createdStationPath.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdStationPath);
        } catch (Exception e) {
            logger.error("Failed to create station path", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<StationPath> updateStationPath(@PathVariable Long id,
                                                         @RequestBody StationPath updatedStationPath) {
        try {
            StationPath existingStationPath = stationPathRepository.findById(id)
                    .orElseThrow(() -> {
                        String errorMessage = "Station path not found";
                        logger.error(errorMessage);
                        return new IllegalArgumentException(errorMessage);
                    });

            existingStationPath.setName(updatedStationPath.getName());
            existingStationPath.setStation(updatedStationPath.getStation());

            StationPath savedStationPath = stationPathRepository.save(existingStationPath);
            logger.info("Updated station path with ID: {}", id);
            return ResponseEntity.ok(savedStationPath);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to update station path with ID: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStationPath(@PathVariable Long id) {
        try {
            StationPath existingStationPath = stationPathRepository.findById(id)
                    .orElseThrow(() -> {
                        String errorMessage = "Station path not found";
                        logger.error(errorMessage);
                        return new IllegalArgumentException(errorMessage);
                    });

            stationPathRepository.delete(existingStationPath);
            logger.info("Deleted station path with ID: {}", id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            logger.error("Failed to delete station path with ID: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }
}
