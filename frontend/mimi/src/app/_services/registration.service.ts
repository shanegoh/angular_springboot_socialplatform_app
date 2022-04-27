import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegistrationResponse } from '../_models/response/registration-response';
import { User } from '../_models/user';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  private baseURL = "http://localhost:8080"

  constructor(private http: HttpClient) { }

  register(user: User) {
    return this.http.post<RegistrationResponse>(this.baseURL + "/api/register", user)
  }
}
