import { Injectable } from '@angular/core';
import {jwtDecode} from "jwt-decode";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private clientId = '330gdd6e15n9uoi1up7tc0nqkl';

  initiateLogin(): void {
    const loginUrl = `https://tic-tac-toe-pwr.auth.us-east-1.amazoncognito.com/oauth2/authorize?client_id=${this.clientId}&response_type=token&scope=email+openid+profile&redirect_uri=http%3A%2F%2Flocalhost%3A4200`;
    window.location.href = loginUrl;
  }

  isAuthenticated(): boolean {
    const idToken = localStorage.getItem('id_token');
    if (idToken) {
      const tokenInfo: any = jwtDecode(idToken);
      const currentTime = Date.now() / 1000;
      return tokenInfo && tokenInfo.exp && tokenInfo.exp > currentTime;
    } else {
      return false;
    }
  }

  logout(): void {
    localStorage.removeItem('id_token');
    localStorage.removeItem('access_token');
    localStorage.removeItem('username');
    window.location.href = 'https://tic-tac-toe-pwr.auth.us-east-1.amazoncognito.com/logout?client_id=330gdd6e15n9uoi1up7tc0nqkl&logout_uri=http%3A%2F%2Flocalhost%3A4200'
  }

  setUsername(accessToken: string) {
    const tokenInfo: any = jwtDecode(accessToken);
    console.log(tokenInfo);
    localStorage.setItem('username', tokenInfo.username);
  }
}
