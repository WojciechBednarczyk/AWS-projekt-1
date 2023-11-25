import { Component } from '@angular/core';
import {AuthenticationService} from "../service/authentication.service";

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.scss']
})
export class NavBarComponent{

  constructor(private authService: AuthenticationService) {
  }

  logout() {
    this.authService.logout();
  }
}
