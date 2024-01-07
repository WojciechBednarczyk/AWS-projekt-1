package pl.edu.pwr.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pwr.backend.mapper.MoveMapper;
import pl.edu.pwr.backend.model.Game;
import pl.edu.pwr.backend.model.GameStatus;
import pl.edu.pwr.backend.model.Move;
import pl.edu.pwr.backend.model.dto.GameDto;
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

    private final WebSocketService webSocketService;

    private int[][] winCombinations = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};

    public GameDto connectToRandomGame(String username) {
        var player = playerRepository.findByPlayerName(username).orElseThrow();
        Game game = gameRepository.findAllGamesWaitingForPlayers().stream().findFirst().orElse(null);
        if (Objects.isNull(game) || game.getPlayerO().equals(player)) {
            game = new Game();
            game.setPlayerO(player);
            game.setStatus(GameStatus.WAITING_FOR_PLAYERS);
            game.setCurrentMove('O');
            gameRepository.save(game);
            var gameDto = new GameDto(game.getGameId(), game.getPlayerO().getPlayerName(), null, null, game.getStatus().toString(), "O", MoveMapper.mapMovesToDto(game.getMoves()));
            notifyFrontend(gameDto);
            return gameDto;
        } else {
            game.setPlayerX(player);
            game.setStatus(GameStatus.IN_PROGRESS);
            gameRepository.save(game);
            var winner = Objects.isNull(game.getWinner()) ? null : game.getWinner().getPlayerName();
            var gameDto = new GameDto(game.getGameId(), game.getPlayerO().getPlayerName(), game.getPlayerX().getPlayerName(), winner, game.getStatus().toString(), "O", MoveMapper.mapMovesToDto(game.getMoves()));
            notifyFrontend(gameDto);
            return gameDto;
        }
    }

    public GameDto makeMove(MoveDto moveDto) {
        var game = gameRepository.findById(moveDto.gameId()).orElseThrow();
        var player = playerRepository.findByPlayerName(moveDto.playerName()).orElseThrow();
        var move = Move.builder()
                .positionX(moveDto.positionX())
                .positionY(moveDto.positionY())
                .player(player)
                .game(game)
                .stamp(moveDto.stamp().charAt(0))
                .build();
        game.getMoves().add(move);
        var currentMove = game.getCurrentMove() == 'O' ? 'X' : 'O';
        game.setCurrentMove(currentMove);
        moveRepository.save(move);
        gameRepository.save(game);
        var gameDto = new GameDto(game.getGameId(), game.getPlayerO().getPlayerName(), game.getPlayerX().getPlayerName(), null, game.getStatus().toString(), String.valueOf(currentMove), MoveMapper.mapMovesToDto(game.getMoves()));
        notifyFrontend(gameDto);
        return gameDto;
    }

    private void notifyFrontend(GameDto gameDto) {
        if (gameDto == null) {
            return;
        }
        webSocketService.sendMessage(gameDto.gameId().toString());
    }

    public GameDto getBoard(Long gameId) {
        var game = gameRepository.findById(gameId).orElseThrow();
        var winner = Objects.isNull(game.getWinner()) ? null : game.getWinner().getPlayerName();
        return new GameDto(game.getGameId(), game.getPlayerO().getPlayerName(), game.getPlayerX().getPlayerName(), winner, game.getStatus().toString(),String.valueOf(game.getCurrentMove()), MoveMapper.mapMovesToDto(game.getMoves()));
    }
}
