package pl.edu.pwr.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long gameId;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "game", cascade = CascadeType.ALL)
    private List<Move> moves;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_x_id")
    private Player playerX;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_o_id")
    private Player playerO;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "winner")
    private Player winner;
}
