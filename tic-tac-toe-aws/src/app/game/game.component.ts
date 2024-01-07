import {Component, OnDestroy, OnInit} from '@angular/core';
import {RestService} from "../service/rest.service";
import {Game} from "./game";
import {SocketService} from "../service/socket.service";
import {ActivatedRoute} from "@angular/router";
import {Move} from "./move";

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit, OnDestroy {

  game!: Game;

  constructor(private restService: RestService,
              private socketService: SocketService,
              private route: ActivatedRoute) {
  }

  ngOnDestroy(): void {
    var subscriptionId = localStorage.getItem('subscription_id')
    if (subscriptionId){
      this.socketService.unsubscribe(subscriptionId);
    }
  }


  ngOnInit(): void {
    this.game = this.route.snapshot.data['game'];
    this.socketService.subscribe('/topic/'+this.game.gameId, (): void => {
      this.updateBoard();
      console.log("Dziala")
    });
  }

  // currentPlayer: string = this.playerOName;
  board: string[][] = [
    ['', '', ''],
    ['', '', ''],
    ['', '', '']
  ];
  // winner: string | null = null;
  //
  // makeMove(row: number, col: number): void {
  //   if (!this.board[row][col] && !this.winner) {
  //     this.board[row][col] = this.currentPlayer === this.playerXName ? 'X' : 'O';
  //     this.currentPlayer = this.currentPlayer === this.playerXName ? this.playerOName : this.playerXName;
  //   }
  // }
  //
  // isBoardFull(): boolean {
  //   return this.board.every(row => row.every(cell => cell !== ''));
  // }
  //
  // restartGame(): void {
  //   this.currentPlayer = 'X';
  //   this.board = [
  //     ['', '', ''],
  //     ['', '', ''],
  //     ['', '', '']
  //   ];
  //   this.winner = null;
  // }

  private updateBoard() {
    this.restService.getUpdatedGame(this.game.gameId).subscribe(response => {
      this.game=response;
      this.game.moves.forEach(move => {
        this.board[move.positionX][move.positionY] = move.stamp;
      })
    });
  }

  makeMove(i: number, j: number) {
    if (this.isMoveValid()) {
      const move: Move = {
        gameId: this.game.gameId,
        playerName: localStorage.getItem('username')!,
        positionX: i,
        positionY: j,
        stamp: this.game.currentMove
      }
      this.restService.makeMove(move).subscribe(response => {
        this.game=response;
        this.game.moves.forEach(move => {
          this.board[move.positionX][move.positionY] = move.stamp;
        })
      })
    }
  }

  private isMoveValid() {
    if (this.game.currentMove === "O" && this.game.playerO === localStorage.getItem('username')) {
      return true;
    } else return this.game.currentMove === "X" && this.game.playerX === localStorage.getItem('username');
  }
}
