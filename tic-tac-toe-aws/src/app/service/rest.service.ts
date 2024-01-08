import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Move} from "../game/move";

@Injectable({
  providedIn: 'root'
})
export class RestService {

  private apiUrl = 'http://localhost:8080/game/';

  constructor(private http: HttpClient) { }

  connectToRandomGame(username: string): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const body = {};

    return this.http.post(this.apiUrl+'connect/random?username='+username, body, { headers });
  }

  getUpdatedGame(gameId: number): Observable<any> {
    return this.http.get(this.apiUrl+'board?gameId='+gameId);
  }

  makeMove(move: Move): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<Move>(this.apiUrl+'move', move, { headers });
  }

  getLeaderBoard() : Observable<any> {
    return this.http.get(this.apiUrl+'leaderboard');
  }
}
