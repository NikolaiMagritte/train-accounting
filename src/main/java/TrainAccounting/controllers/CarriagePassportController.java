package TrainAccounting.controllers;

import TrainAccounting.model.CarriagePassport;
import TrainAccounting.repositories.CarriagePassportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@RestController
@RequestMapping("/api/carriage-passports")
@RequiredArgsConstructor
public class CarriagePassportController {
    private static final Logger logger = LoggerFactory.getLogger(CarriagePassportController.class);

    private final CarriagePassportRepository carriagePassportRepository;

    @GetMapping
    public ResponseEntity<List<CarriagePassport>> getAllCarriagePassports() {
        List<CarriagePassport> carriagePassports = carriagePassportRepository.findAll();
        if (!carriagePassports.isEmpty()) {
            logger.info("Retrieved all carriage passports");
            return ResponseEntity.ok().body(carriagePassports);
        } else {
            logger.info("No carriage passports found");
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarriagePassport> getCarriagePassportById(@PathVariable Long id) {
        try {
            CarriagePassport existingCarriagePassport = carriagePassportRepository.findById(id)
                    .orElseThrow(() -> {
                        String errorMessage = "Carriage passport not found";
                        logger.error(errorMessage);
                        return new IllegalArgumentException(errorMessage);
                    });
            logger.info("Retrieved carriage passport with ID: {}", id);
            return ResponseEntity.ok().body(existingCarriagePassport);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to retrieve carriage passport with ID: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<CarriagePassport> createCarriagePassport(@RequestBody CarriagePassport carriagePassport) {
        try {
            CarriagePassport createdCarriagePassport = carriagePassportRepository.save(carriagePassport);
            logger.info("Created carriage passport with ID: {}", createdCarriagePassport.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCarriagePassport);
        } catch (Exception e) {
            logger.error("Failed to create carriage passport", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarriagePassport> updateCarriagePassport(@PathVariable Long id,
                                                                   @RequestBody CarriagePassport updatedCarriagePassport) {
        try {
            CarriagePassport existingCarriagePassport = carriagePassportRepository.findById(id)
                    .orElseThrow(() -> {
                        String errorMessage = "Carriage passport not found";
                        logger.error(errorMessage);
                        return new IllegalArgumentException(errorMessage);
                    });

            existingCarriagePassport.setNumber(updatedCarriagePassport.getNumber());
            existingCarriagePassport.setCarriageType(updatedCarriagePassport.getCarriageType());
            existingCarriagePassport.setCargoWeight(updatedCarriagePassport.getCargoWeight());
            existingCarriagePassport.setCargoCapacity(updatedCarriagePassport.getCargoCapacity());

            CarriagePassport savedCarriagePassport = carriagePassportRepository.save(existingCarriagePassport);
            logger.info("Updated carriage passport with ID: {}", id);
            return ResponseEntity.ok().body(savedCarriagePassport);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to update carriage passport with ID: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarriagePassport(@PathVariable Long id) {
        try {
            CarriagePassport existingCarriagePassport = carriagePassportRepository.findById(id)
                    .orElseThrow(() -> {
                        String errorMessage = "Carriage passport not found";
                        logger.error(errorMessage);
                        return new IllegalArgumentException(errorMessage);
                    });

            carriagePassportRepository.delete(existingCarriagePassport);
            logger.info("Deleted carriage passport with ID: {}", id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            logger.error("Failed to delete carriage passport with ID: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }
}

