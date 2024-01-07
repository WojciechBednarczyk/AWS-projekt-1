package pl.edu.pwr.backend.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import pl.edu.pwr.backend.model.Game;
import pl.edu.pwr.backend.model.dto.MoveDto;
import pl.edu.pwr.backend.service.GameService;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/connect/random")
    public ResponseEntity<Game> connectRandom(@RequestParam String username) {
        return ResponseEntity.ok(gameService.connectToRandomGame(username));
    }

    @PostMapping("/move")
    public ResponseEntity<Game> makeMove(@RequestParam MoveDto move) {
        Game game = gameService.makeMove(move);
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }
}
