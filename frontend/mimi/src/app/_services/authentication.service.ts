import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders} from "@angular/common/http";
import { Observable, BehaviorSubject } from "rxjs";
import { JwtHelperService } from '@auth0/angular-jwt';
import { AuthenticationResponse } from '../_models/response/authentication-response';
import { JWTService } from './jwt.service';

@Injectable({
  providedIn: 'root'
})



export class AuthenticationService {

  private baseURL = "http://localhost:8080"

  jsonWebToken: String | undefined;

  constructor(private http: HttpClient, private jwtService: JWTService) { }

  login(username:string, password:string ) {
    return this.http.post<AuthenticationResponse>(this.baseURL + "/api/authenticate", {username, password})
  }

  public isAuthenticated(): boolean {
    // Check whether the token is expired and return true or false
    return !this.jwtService.getTokenExpireStatus()
  }

  public hasToken(): boolean {
    return this.jwtService.getToken() !== null ? true : false
  }

  public getToken(): string {
    return this.jwtService.getToken()!
  }

  public getRole(): string {
    return this.jwtService.getRole()!
  }
}
