package TrainAccounting.services;

import TrainAccounting.model.CarriagePassport;
import TrainAccounting.model.CarriageReceipt;
import TrainAccounting.model.Station;
import TrainAccounting.model.StationPath;
import TrainAccounting.repositories.CarriagePassportRepository;
import TrainAccounting.repositories.CarriageReceiptRepository;
import TrainAccounting.repositories.StationPathRepository;
import TrainAccounting.repositories.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AcceptCarriragesServiceImpl implements AcceptCarriagesService{
    private final CarriagePassportRepository carriagePassportRepository;
    private final StationRepository stationRepository;
    private final StationPathRepository stationPathRepository;
    private final CarriageReceiptRepository carriageReceiptRepository;

    @Override
    public void acceptCarriages(List<CarriagePassport> carriages, Station station, StationPath stationPath) {
        Station existingStation = stationRepository.findById(station.getId())
                .orElseThrow(() -> new IllegalArgumentException("Station not found"));
        StationPath existingPath = stationPathRepository.findById(stationPath.getId())
                .orElseThrow(() -> new IllegalArgumentException("StationPath not found"));

        if (!existingPath.getStation().equals(existingStation)) {
            throw new IllegalArgumentException("Station path does not belong to the Station");
        }

        List<CarriagePassport> existingCarriages = existingPath.getCarriages();

        int lastSequenceNumber = existingCarriages.isEmpty() ? 0 : existingCarriages.get(existingCarriages.size() - 1).getSequenceNumber();


        List<CarriagePassport> acceptedCarriages = new ArrayList<>();

        for (CarriagePassport carriage : carriages) {
            carriage.setSequenceNumber(lastSequenceNumber + 1);
            carriage.setStationPath(existingPath);
            carriagePassportRepository.save(carriage);
            acceptedCarriages.add(carriage);
            existingPath.getCarriages().add(carriage);
        }

        CarriageReceipt carriageReceipt = new CarriageReceipt(acceptedCarriages);
        carriageReceiptRepository.save(carriageReceipt);

        stationPathRepository.save(existingPath);
    }
}
