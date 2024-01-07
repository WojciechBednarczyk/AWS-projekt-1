import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
import {Observable} from 'rxjs';
import {RestService} from "../service/rest.service";

@Injectable({
  providedIn: 'root'
})
export class GameResolver implements Resolve<any> {
  constructor(private restService: RestService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
    const username = localStorage.getItem('username');
    return this.restService.connectToRandomGame(username!);
  }
}
