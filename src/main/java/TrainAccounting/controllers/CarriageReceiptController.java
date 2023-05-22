package TrainAccounting.controllers;

import TrainAccounting.model.CarriageReceipt;
import TrainAccounting.repositories.CarriageReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/carriage-receipts")
@RequiredArgsConstructor
public class CarriageReceiptController {
    private static final Logger logger = LoggerFactory.getLogger(CarriageReceiptController.class);

    private final CarriageReceiptRepository carriageReceiptRepository;

    @GetMapping
    public ResponseEntity<List<CarriageReceipt>> getAllCarriageReceipts() {
        List<CarriageReceipt> carriageReceipts = carriageReceiptRepository.findAll();
        if (!carriageReceipts.isEmpty()) {
            logger.info("Retrieved all carriage receipts");
            return ResponseEntity.ok().body(carriageReceipts);
        } else {
            logger.info("No carriage receipts found");
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarriageReceipt> getCarriageReceiptById(@PathVariable Long id) {
        try {
            CarriageReceipt carriageReceipt = carriageReceiptRepository.findById(id)
                    .orElseThrow(() -> {
                        String errorMessage = "Carriage receipt not found";
                        logger.error(errorMessage);
                        return new IllegalArgumentException(errorMessage);
                    });
            logger.info("Retrieved carriage receipt with ID: {}", id);
            return ResponseEntity.ok(carriageReceipt);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to retrieve carriage receipt with ID: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<CarriageReceipt> createCarriageReceipt(@RequestBody CarriageReceipt carriageReceipt) {
        try {
            CarriageReceipt createdCarriageReceipt = carriageReceiptRepository.save(carriageReceipt);
            logger.info("Created carriage receipt with ID: {}", createdCarriageReceipt.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCarriageReceipt);
        } catch (Exception e) {
            logger.error("Failed to create carriage receipt", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarriageReceipt> updateCarriageReceipt(@PathVariable Long id,
                                                                 @RequestBody CarriageReceipt updatedCarriageReceipt) {
        try {
            CarriageReceipt existingCarriageReceipt = carriageReceiptRepository.findById(id)
                    .orElseThrow(() -> {
                        String errorMessage = "Carriage receipt not found";
                        logger.error(errorMessage);
                        return new IllegalArgumentException(errorMessage);
                    });

            existingCarriageReceipt.setCarriages(updatedCarriageReceipt.getCarriages());

            CarriageReceipt savedCarriageReceipt = carriageReceiptRepository.save(existingCarriageReceipt);
            logger.info("Updated carriage receipt with ID: {}", id);
            return ResponseEntity.ok(savedCarriageReceipt);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to update carriage receipt with ID: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarriageReceipt(@PathVariable Long id) {
        try {
            CarriageReceipt existingCarriageReceipt = carriageReceiptRepository.findById(id)
                    .orElseThrow(() -> {
                        String errorMessage = "Carriage receipt not found";
                        logger.error(errorMessage);
                        return new IllegalArgumentException(errorMessage);
                    });

            carriageReceiptRepository.delete(existingCarriageReceipt);
            logger.info("Deleted carriage receipt with ID: {}", id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            logger.error("Failed to delete carriage receipt with ID: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }
}

