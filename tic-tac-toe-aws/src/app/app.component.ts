import {Component} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {AuthenticationService} from "./service/authentication.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'tic-tac-toe-aws';

  constructor(private route: ActivatedRoute,
              private authService: AuthenticationService) {
  }

  ngOnInit(): void {
    if (!this.authService.isAuthenticated()) {
      this.authService.initiateLogin();
      this.route.fragment.subscribe(fragment => {
        if (fragment) {
          const tokenParams = new URLSearchParams(fragment);
          const idToken = tokenParams.get('id_token');
          const accessToken = tokenParams.get('access_token');
          if (idToken) {
            localStorage.setItem('id_token', idToken);
          }
          if (accessToken) {
            localStorage.setItem('access_token', accessToken);
            this.authService.setUsername(accessToken);
          }
          window.location.href = window.location.origin;
        }
      });
    }
  }
}
