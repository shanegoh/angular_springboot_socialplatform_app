import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AccountPagination } from '../_models/response/account-pagination';
import { GenericResponse } from '../_models/response/generic-response';
import { JWTService } from './jwt.service';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  private baseURL = "http://localhost:8080"

  jsonWebToken: String | undefined;

  constructor(private http: HttpClient) { }

  getAllUserAccountByPage(page: number) {
    return this.http.get<AccountPagination>(`${this.baseURL}/api/admin/getAllUserAccount/${page}`)
  }

  updateAccountStatus(id: number, status: number) {
    return this.http.put<GenericResponse>(`${this.baseURL}/api/admin/updateAccountStatus/${id}/${status}`,{})
  }

  searchAccountByKeyword(page: number, searchText: string) {
    return this.http.get<AccountPagination>(`${this.baseURL}/api/admin/searchAccount/${searchText}/page/${page}`)
  }
}
