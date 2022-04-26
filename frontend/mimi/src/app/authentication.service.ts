import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders} from "@angular/common/http";
import { Observable, BehaviorSubject } from "rxjs";
import { JwtHelperService } from '@auth0/angular-jwt';
import { AuthenticationResponse } from './authentication-response';

@Injectable({
  providedIn: 'root'
})



export class AuthenticationService {

  private baseURL = "http://localhost:8080"

  jsonWebToken: String | undefined;

  constructor(private http: HttpClient, public jwtHelper: JwtHelperService) { }

  login(username:string, password:string ) {
    return this.http.post<AuthenticationResponse>(this.baseURL + "/api/authenticate", {username, password})
  }

  public isAuthenticated(): boolean {
    const token = localStorage.getItem('token');
    // Check whether the token is expired and return
    // true or false
    return !this.jwtHelper.isTokenExpired(token!);
  }

}
