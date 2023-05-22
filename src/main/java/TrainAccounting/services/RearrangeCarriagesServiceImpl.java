package TrainAccounting.services;

import TrainAccounting.model.CarriagePassport;
import TrainAccounting.model.CarriageReceipt;
import TrainAccounting.model.Station;
import TrainAccounting.model.StationPath;
import TrainAccounting.repositories.CarriagePassportRepository;
import TrainAccounting.repositories.StationPathRepository;
import TrainAccounting.repositories.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RearrangeCarriagesServiceImpl implements RearrangeCarriagesService {
    private static final Logger logger = LoggerFactory.getLogger(RearrangeCarriagesServiceImpl.class);

    private final CarriagePassportRepository carriagePassportRepository;
    private final StationRepository stationRepository;
    private final StationPathRepository stationPathRepository;

    @Override
    public void rearrangeCarriages(List<CarriagePassport> carriages, Station station,
                                   StationPath sourcePath, StationPath destinationPath) {
        Station existingStation = stationRepository.findById(station.getId())
                .orElseThrow(() -> {
                    String errorMessage = "Station not found";
                    logger.error(errorMessage);
                    return new IllegalArgumentException(errorMessage);
                });

        StationPath existingSourcePath = stationPathRepository.findById(sourcePath.getId())
                .orElseThrow(() -> {
                    String errorMessage = "Source StationPath not found";
                    logger.error(errorMessage);
                    return new IllegalArgumentException(errorMessage);
                });

        StationPath existingDestinationPath = stationPathRepository.findById(destinationPath.getId())
                .orElseThrow(() -> {
                    String errorMessage = "Destination StationPath not found";
                    logger.error(errorMessage);
                    return new IllegalArgumentException(errorMessage);
                });

        if (!existingSourcePath.getStation().equals(existingStation)
                || !existingDestinationPath.getStation().equals(existingStation)) {
            String errorMessage = "StationPath does not belong to the Station";
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        List<CarriagePassport> existingSourceCarriages = existingSourcePath.getCarriages();
        existingSourceCarriages.removeAll(carriages);
        stationPathRepository.save(existingSourcePath);

        List<CarriagePassport> existingCarriages = existingDestinationPath.getCarriages();

        int lastSequenceNumber = existingCarriages.isEmpty() ? 0 : existingCarriages.get(existingCarriages.size() - 1).getSequenceNumber();

        for (CarriagePassport carriage : carriages) {
            carriage.setSequenceNumber(lastSequenceNumber + 1);
            carriage.setStationPath(existingDestinationPath);
            carriagePassportRepository.save(carriage);
            existingDestinationPath.getCarriages().add(carriage);
        }

        stationPathRepository.save(existingDestinationPath);

        logger.info("Carriages rearranged: {}", carriages);
        logger.info("Carriages moved from source path: {} to destination path: {}", sourcePath.getId(), destinationPath.getId());
    }
}

