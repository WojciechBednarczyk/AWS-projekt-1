package pl.edu.pwr.backend.mapper;

import pl.edu.pwr.backend.model.Move;
import pl.edu.pwr.backend.model.dto.MoveDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MoveMapper {

    private MoveMapper() {
    }

    public static List<MoveDto> mapMovesToDto(List<Move> moves) {
        if (Objects.isNull(moves)) {
            return new ArrayList<>();
        } else {
            return moves.stream().map(move -> new MoveDto(
                    move.getPositionX(),
                    move.getPositionY(),
                    move.getGame().getGameId(),
                    move.getPlayer().getPlayerName(),
                    String.valueOf(move.getStamp())
            )).toList();
        }
    }
}
