export interface Move {
  positionX: number,
  positionY: number,
  gameId: number,
  playerName: string,
  stamp: 'O' | 'X'
}
