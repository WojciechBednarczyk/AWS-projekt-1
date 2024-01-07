package pl.edu.pwr.backend.model.dto;

import java.util.List;

public record GameDto(
        Long gameId,
        String playerO,
        String playerX,
        String winner,
        String status,
        String currentMove,
        List<MoveDto> moves
) {
}
