import {Move} from "./move";

export interface Game {
  gameId: number,
  playerO: string,
  playerX: string,
  winner: string,
  status: string,
  currentMove: 'O' | 'X',
  moves: Move[];
}
