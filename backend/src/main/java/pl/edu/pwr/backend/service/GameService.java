package pl.edu.pwr.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pwr.backend.model.Game;
import pl.edu.pwr.backend.model.GameStatus;
import pl.edu.pwr.backend.model.Move;
import pl.edu.pwr.backend.model.dto.MoveDto;
import pl.edu.pwr.backend.repository.GameRepository;
import pl.edu.pwr.backend.repository.MoveRepository;
import pl.edu.pwr.backend.repository.PlayerRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    private final PlayerRepository playerRepository;

    private final MoveRepository moveRepository;

    private int[][] winCombinations = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
    public Game connectToRandomGame(String username) {
        var player = playerRepository.findByPlayerName(username).orElseThrow();
        Game game = gameRepository.findAllGamesWaitingForPlayers().stream().findFirst().orElse(null);
        if (Objects.isNull(game) || game.getPlayerO().equals(player)) {
            Game newGame = new Game();
            newGame.setPlayerO(player);
            newGame.setStatus(GameStatus.WAITING_FOR_PLAYERS);
            gameRepository.save(newGame);
            return newGame;
        } else {
            game.setPlayerX(player);
            game.setStatus(GameStatus.IN_PROGRESS);
            gameRepository.save(game);
            return game;
        }
    }

    public Game makeMove(MoveDto moveDto) {
        var game = gameRepository.findById(moveDto.gameId()).orElseThrow();
        var player = playerRepository.findByPlayerName(moveDto.playerName()).orElseThrow();
        var move = Move.builder()
                .positionX(moveDto.positionX())
                .positionY(moveDto.positionY())
                .player(player)
                .game(game)
                .build();
        game.getMoves().add(move);
        moveRepository.save(move);
        return game;
    }
}
