package TrainAccounting.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class StationPath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @OneToMany(mappedBy = "stationPath", cascade = CascadeType.ALL)
    private List<CarriagePassport> carriages = new ArrayList<>();

    public StationPath(String name, Station station) {
        this.name = name;
        this.station = station;
    }

    public StationPath() {
    }
}
