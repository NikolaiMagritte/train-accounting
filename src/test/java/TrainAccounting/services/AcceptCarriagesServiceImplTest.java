package TrainAccounting.services;
import TrainAccounting.model.CarriagePassport;
import TrainAccounting.model.CarriageReceipt;
import TrainAccounting.model.Station;
import TrainAccounting.model.StationPath;
import TrainAccounting.repositories.CarriagePassportRepository;
import TrainAccounting.repositories.CarriageReceiptRepository;
import TrainAccounting.repositories.StationPathRepository;
import TrainAccounting.repositories.StationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AcceptCarriagesServiceImplTest {
    @Mock
    private CarriagePassportRepository carriagePassportRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private StationPathRepository stationPathRepository;

    @Mock
    private CarriageReceiptRepository carriageReceiptRepository;

    private AcceptCarriagesServiceImpl acceptCarriagesService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        acceptCarriagesService = new AcceptCarriagesServiceImpl(
                carriagePassportRepository,
                stationRepository,
                stationPathRepository,
                carriageReceiptRepository
        );
    }

    @Test
    public void testAcceptCarriages_StationNotFound() {
        Long stationId = 1L;
        Long stationPathId = 2L;
        StationPath existingPath = new StationPath();
        existingPath.setId(stationPathId);

        Mockito.when(stationRepository.findById(stationId)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                acceptCarriagesService.acceptCarriages(new ArrayList<>(), new Station(stationId), existingPath)
        );
    }

    @Test
    public void testAcceptCarriages_StationPathNotFound() {
        Long stationId = 1L;
        Long stationPathId = 2L;
        Station existingStation = new Station(stationId);

        Mockito.when(stationRepository.findById(stationId)).thenReturn(Optional.of(existingStation));
        Mockito.when(stationPathRepository.findById(stationPathId)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                acceptCarriagesService.acceptCarriages(new ArrayList<>(), existingStation, new StationPath(stationPathId))
        );
    }

    @Test
    public void testAcceptCarriages_StationPathNotBelongToStation() {
        Long stationId = 1L;
        Long stationPathId = 2L;
        Station existingStation = new Station(stationId);
        StationPath existingPath = new StationPath();
        existingPath.setId(stationPathId);
        existingPath.setStation(new Station(stationId + 1)); // Different station ID

        Mockito.when(stationRepository.findById(stationId)).thenReturn(Optional.of(existingStation));
        Mockito.when(stationPathRepository.findById(stationPathId)).thenReturn(Optional.of(existingPath));

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                acceptCarriagesService.acceptCarriages(new ArrayList<>(), existingStation, existingPath)
        );
    }

    @Test
    public void testAcceptCarriages_Success() {
        Long stationId = 1L;
        Long stationPathId = 2L;
        Station existingStation = new Station(stationId);
        StationPath existingPath = new StationPath();
        existingPath.setId(stationPathId);
        existingPath.setStation(existingStation);

        Mockito.when(stationRepository.findById(stationId)).thenReturn(Optional.of(existingStation));
        Mockito.when(stationPathRepository.findById(stationPathId)).thenReturn(Optional.of(existingPath));

        List<CarriagePassport> carriages = new ArrayList<>();
        CarriagePassport carriage1 = new CarriagePassport();
        carriages.add(carriage1);
        acceptCarriagesService.acceptCarriages(carriages, existingStation, existingPath);

        Mockito.verify(carriagePassportRepository, Mockito.times(1)).save(carriage1);
        Mockito.verify(stationPathRepository, Mockito.times(1)).save(existingPath);
        Mockito.verify(carriageReceiptRepository, Mockito.times(1)).save(Mockito.any(CarriageReceipt.class));
    }
}