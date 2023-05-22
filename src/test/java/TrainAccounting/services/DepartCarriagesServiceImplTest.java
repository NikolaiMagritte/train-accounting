package TrainAccounting.services;

import TrainAccounting.model.CarriagePassport;
import TrainAccounting.model.Station;
import TrainAccounting.model.StationPath;
import TrainAccounting.repositories.CarriagePassportRepository;
import TrainAccounting.repositories.StationPathRepository;
import TrainAccounting.repositories.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DepartCarriagesServiceImplTest {
    @Mock
    private CarriagePassportRepository carriagePassportRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private StationPathRepository stationPathRepository;

    @InjectMocks
    private DepartCarriagesServiceImpl departCarriagesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDepartCarriagesWithValidData() {
        Station station = new Station(1L, "Station A");
        StationPath stationPath = new StationPath(1L, "Path 1", station, new ArrayList<>());
        List<CarriagePassport> carriages = new ArrayList<>();
        CarriagePassport carriage1 = new CarriagePassport(1L, 1, 1);
        CarriagePassport carriage2 = new CarriagePassport(2L, 2, 2);
        CarriagePassport carriage3 = new CarriagePassport(3L, 3, 3);
        carriages.add(carriage1);
        carriages.add(carriage2);
        carriages.add(carriage3);

        stationPath.getCarriages().add(carriage1);
        stationPath.getCarriages().add(carriage2);
        stationPath.getCarriages().add(carriage3);

        when(stationRepository.findById(station.getId())).thenReturn(Optional.of(station));
        when(stationPathRepository.findById(stationPath.getId())).thenReturn(Optional.of(stationPath));

        assertDoesNotThrow(() -> departCarriagesService.departCarriages(carriages, station, stationPath));

        assertEquals(0, stationPath.getCarriages().size());
        assertNull(carriage1.getStationPath());
        assertEquals(0, carriage1.getSequenceNumber());
        assertNull(carriage2.getStationPath());
        assertEquals(0, carriage2.getSequenceNumber());
        assertNull(carriage3.getStationPath());
        assertEquals(0, carriage3.getSequenceNumber());

        verify(carriagePassportRepository, times(3)).save(any(CarriagePassport.class));
    }
}
