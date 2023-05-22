package TrainAccounting.controllers;

import TrainAccounting.model.Cargo;
import TrainAccounting.repositories.CargoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/cargos")
@RequiredArgsConstructor
public class CargoController {
    private static final Logger logger = LoggerFactory.getLogger(CargoController.class);

    private final CargoRepository cargoRepository;

    @GetMapping
    public ResponseEntity<List<Cargo>> getAllCargos() {
        List<Cargo> cargos = cargoRepository.findAll();
        if (!cargos.isEmpty()) {
            logger.info("Retrieved all cargos");
            return ResponseEntity.ok().body(cargos);
        } else {
            logger.info("No cargos found");
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cargo> getCargoById(@PathVariable Long id) {
        try {
            Cargo cargo = cargoRepository.findById(id)
                    .orElseThrow(() -> {
                        String errorMessage = "Cargo not found";
                        logger.error(errorMessage);
                        return new IllegalArgumentException(errorMessage);
                    });
            logger.info("Retrieved cargo with ID: {}", id);
            return ResponseEntity.ok(cargo);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to retrieve cargo with ID: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Cargo> createCargo(@RequestBody Cargo cargo) {
        try {
            Cargo createdCargo = cargoRepository.save(cargo);
            logger.info("Created cargo with ID: {}", createdCargo.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(cargo);
        } catch (Exception e) {
            logger.error("Failed to create cargo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cargo> updateCargo(@PathVariable Long id, @RequestBody Cargo updatedCargo) {
        try {
            Cargo existingCargo = cargoRepository.findById(id)
                    .orElseThrow(() -> {
                        String errorMessage = "Cargo not found";
                        logger.error(errorMessage);
                        return new IllegalArgumentException(errorMessage);
                    });
            existingCargo.setName(updatedCargo.getName());
            Cargo savedCargo = cargoRepository.save(existingCargo);
            logger.info("Updated cargo with ID: {}", id);
            return ResponseEntity.ok(savedCargo);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to update cargo with ID: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCargo(@PathVariable Long id) {
        try {
            Cargo existingCargo = cargoRepository.findById(id)
                    .orElseThrow(() -> {
                        String errorMessage = "Cargo not found";
                        logger.error(errorMessage);
                        return new IllegalArgumentException(errorMessage);
                    });
            cargoRepository.delete(existingCargo);
            logger.info("Deleted cargo with ID: {}", id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            logger.error("Failed to delete cargo with ID: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }
}

