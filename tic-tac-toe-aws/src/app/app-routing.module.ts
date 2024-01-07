import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {GameComponent} from "./game/game.component";
import {GameResolver} from "./resolver/GameResolver";

const routes: Routes = [
  {
    path: 'game', component: GameComponent, resolve: {
      game: GameResolver
    }
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
