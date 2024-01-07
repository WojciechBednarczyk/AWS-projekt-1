package pl.edu.pwr.backend.model.dto;

public record MoveDto(
        Integer positionX,
        Integer positionY,
        Long gameId,
        String playerName,
        String stamp
) {
}
