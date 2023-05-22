package TrainAccounting.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "station", cascade = CascadeType.ALL)
    private List<StationPath> stationPaths = new ArrayList<>();

    public Station(String name) {
        this.name = name;
    }

    public Station(Long id) {
        this.id = id;
    }

    public Station(List<StationPath> stationPaths) {
        this.stationPaths = stationPaths;
    }

    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Station() {
    }
}
