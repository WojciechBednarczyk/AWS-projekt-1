import { Component } from '@angular/core';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent {

  playerOName: string = 'Gracz 1';
  playerXName: string = 'Gracz 2';
  currentPlayer: string = this.playerOName;
  board: string[][] = [
    ['', '', ''],
    ['', '', ''],
    ['', '', '']
  ];
  winner: string | null = null;

  makeMove(row: number, col: number): void {
    if (!this.board[row][col] && !this.winner) {
      this.board[row][col] = this.currentPlayer === this.playerXName ? 'X' : 'O';
      this.currentPlayer = this.currentPlayer === this.playerXName ? this.playerOName : this.playerXName;
    }
  }

  isBoardFull(): boolean {
    return this.board.every(row => row.every(cell => cell !== ''));
  }

  restartGame(): void {
    this.currentPlayer = 'X';
    this.board = [
      ['', '', ''],
      ['', '', ''],
      ['', '', '']
    ];
    this.winner = null;
  }

}
