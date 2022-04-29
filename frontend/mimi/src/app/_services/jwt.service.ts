import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class JWTService {

  constructor(private jwtHelperService: JwtHelperService) { }

  getToken = () => {
    return localStorage.getItem("TOKEN")
  }

  setTokenDetails = (token: string) => {
    try {
      localStorage.setItem("TOKEN", token)
      const decodedToken = this.jwtHelperService.decodeToken(token)
      this.setRole(decodedToken.isAdmin)
      this.setUsername( decodedToken.sub)
      console.log(token)
      console.log("Expiry Date: " + this.getExpirationDate())
      console.log(decodedToken)
      console.log("Expired: " + this.getTokenExpireStatus())
      console.log("Username: " + decodedToken.sub)
    } catch (e) {
      console.log("Error setting token details.")
    }
  }

  setUsername = (username: string) => {
    localStorage.setItem("USERNAME", username)
  }

  getUsername() {
    return localStorage.getItem("USERNAME")
  }
  
  setExpirationDate = (date: Date) => {
    localStorage.setItem("EXPIRATION_DATE", date.toLocaleString())
  }

  getExpirationDate = () => {
    return this.jwtHelperService.getTokenExpirationDate(localStorage.getItem("TOKEN")!)
  }

  getTokenExpireStatus = () => {
    return this.jwtHelperService.isTokenExpired(localStorage.getItem("TOKEN")!);
  }

  setRole = (role: boolean) => {
    const roleName = role == true ? "ADMIN" : "USER"
    localStorage.setItem("ROLE", roleName)
  }

  getRole = () => {
    return localStorage.getItem("ROLE")
  }
}
