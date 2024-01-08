import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {RestService} from "../service/rest.service";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class LeaderboardResolver implements Resolve<any> {
  constructor(private restService: RestService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
    return this.restService.getLeaderBoard();
  }
}
