import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {GameComponent} from "./game/game.component";
import {GameResolver} from "./resolver/GameResolver";
import {LeaderboardComponent} from "./leaderboard/leaderboard.component";
import {LeaderboardResolver} from "./resolver/LeaderboardResolver";

const routes: Routes = [
  {
    path: 'game', component: GameComponent, resolve: {
      game: GameResolver
    }
  },
  {
    path: 'leaderboard', component: LeaderboardComponent, resolve: {
      leaderboard: LeaderboardResolver
    }
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
