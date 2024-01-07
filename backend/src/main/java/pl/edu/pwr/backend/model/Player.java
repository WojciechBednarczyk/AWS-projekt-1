package pl.edu.pwr.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    private Long playerId;

    private String playerName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "player", cascade = CascadeType.ALL)
    private List<Move> moves;

    @OneToMany(mappedBy = "playerX")
    private List<Game> gamesX;

    @OneToMany(mappedBy = "playerO")
    private List<Game> gamesO;

    @OneToMany(mappedBy = "winner")
    private List<Game> wonGames;

    @Transient
    private List<Game> games;

    @PostLoad
    private void onLoad() {
        games = Stream.concat(gamesX.stream(), gamesO.stream())
                .toList();
    }

}
