package pl.edu.pwr.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pwr.backend.exception.GameIsFinishedException;
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
        try {
            game.getWinner();
        } catch (GameIsFinishedException exception) {
            throw new GameIsFinishedException("The game is over!");
        }
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
        if (checkWinnner(game)) {
            game.setWinner(player);
        }
        var winner = checkWinnner(game) ? player.getPlayerName() : null;
        moveRepository.save(move);
        gameRepository.save(game);
        var gameDto = new GameDto(game.getGameId(), game.getPlayerO().getPlayerName(), game.getPlayerX().getPlayerName(), winner, game.getStatus().toString(), String.valueOf(currentMove), MoveMapper.mapMovesToDto(game.getMoves()));
        notifyFrontend(gameDto);
        return gameDto;
    }

    private boolean checkWinnner(Game game) {
        char[][] board = new char[3][3];

        for (Move move : game.getMoves()) {
            int x = move.getPositionX();
            int y = move.getPositionY();
            char stamp = move.getStamp();
            board[x][y] = stamp;
        }

        // Sprawdź poziomo, pionowo i na ukos
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != '\u0000') {
                return true; // Wygrana w poziomie
            }

            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != '\u0000') {
                return true; // Wygrana w pionie
            }
        }

        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != '\u0000') {
            return true; // Wygrana na ukos (od lewej górnej do prawej dolnej)
        }

        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != '\u0000') {
            return true; // Wygrana na ukos (od prawej górnej do lewej dolnej)
        }

        return false;
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
        return new GameDto(game.getGameId(), game.getPlayerO().getPlayerName(), game.getPlayerX().getPlayerName(), winner, game.getStatus().toString(), String.valueOf(game.getCurrentMove()), MoveMapper.mapMovesToDto(game.getMoves()));
    }
}
