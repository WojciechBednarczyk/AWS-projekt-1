package pl.edu.pwr.backend.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import pl.edu.pwr.backend.model.dto.GameDto;
import pl.edu.pwr.backend.model.dto.MoveDto;
import pl.edu.pwr.backend.service.GameService;

@RestController
@Slf4j
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/connect/random")
    public ResponseEntity<GameDto> connectRandom(@RequestParam String username) {
        return ResponseEntity.ok(gameService.connectToRandomGame(username));
    }

    @PostMapping("/move")
    public ResponseEntity<GameDto> makeMove(@RequestBody MoveDto move) {
        GameDto game = gameService.makeMove(move);

        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.gameId(), game);
        return ResponseEntity.ok(game);
    }

    @GetMapping("/board")
    public ResponseEntity<GameDto> getBoard(@RequestParam Long gameId) {
        return ResponseEntity.ok(gameService.getBoard(gameId));
    }
}
