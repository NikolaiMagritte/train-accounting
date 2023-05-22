package TrainAccounting.controllers;

import TrainAccounting.model.Station;
import TrainAccounting.repositories.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
public class StationController {
    private static final Logger logger = LoggerFactory.getLogger(StationController.class);

    private final StationRepository stationRepository;

    @GetMapping
    public ResponseEntity<List<Station>> getAllStations() {
        List<Station> stations = stationRepository.findAll();
        if (!stations.isEmpty()) {
            logger.info("Retrieved all stations");
            return ResponseEntity.ok().body(stations);
        } else {
            logger.info("No stations found");
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Station> getStationById(@PathVariable Long id) {
        try {
            Station station = stationRepository.findById(id)
                    .orElseThrow(() -> {
                        String errorMessage = "Station not found";
                        logger.error(errorMessage);
                        return new IllegalArgumentException(errorMessage);
                    });
            logger.info("Retrieved station with ID: {}", id);
            return ResponseEntity.ok(station);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to retrieve station with ID: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Station> createStation(@RequestBody Station station) {
        try {
            Station createdStation = stationRepository.save(station);
            logger.info("Created station with ID: {}", createdStation.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdStation);
        } catch (Exception e) {
            logger.error("Failed to create station", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Station> updateStation(@PathVariable Long id,
                                                 @RequestBody Station updatedStation) {
        try {
            Station existingStation = stationRepository.findById(id)
                    .orElseThrow(() -> {
                        String errorMessage = "Station not found";
                        logger.error(errorMessage);
                        return new IllegalArgumentException(errorMessage);
                    });

            existingStation.setName(updatedStation.getName());

            Station savedStation = stationRepository.save(existingStation);
            logger.info("Updated station with ID: {}", id);
            return ResponseEntity.ok(savedStation);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to update station with ID: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        try {
            Station existingStation = stationRepository.findById(id)
                    .orElseThrow(() -> {
                        String errorMessage = "Station not found";
                        logger.error(errorMessage);
                        return new IllegalArgumentException(errorMessage);
                    });

            stationRepository.delete(existingStation);
            logger.info("Deleted station with ID: {}", id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            logger.error("Failed to delete station with ID: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }
}

