import { Component, OnChanges, OnInit } from '@angular/core';
import { Account } from '../_models/account';
import { AccountPagination } from '../_models/response/account-pagination';
import { GenericResponse } from '../_models/response/generic-response';
import { AccountService } from '../_services/account.service';

@Component({
  selector: 'account-info',
  templateUrl: './account-info.component.html',
  styleUrls: ['./account-info.component.css']
})
export class AccountInfoComponent implements OnInit {

  pageNumber: number = 1;
  last: boolean | undefined
  totalElements: number | undefined
  totalPages: number | undefined
  numberOfElements: number | undefined
  pageWithContent: Account[][] = [];
  pageTracker: number[] = []

  constant: number | undefined

  genericResponse: GenericResponse = new GenericResponse();

  constructor(private accountService: AccountService) { }

  ngOnInit(): void {
    this.getPaginationData(1)
    this.pageTracker.push(1)
    if(this.totalElements! > 10) {
      this.constant = 1
    } else {
      this.constant = 10
    }
  }

  verifyLoadStatus() {
    if(!this.pageTracker.includes(this.pageNumber)) {
      this.getPaginationData(this.pageNumber)
    } 
  }

  getPaginationData(page: number) {
    this.pageTracker.push(page)
    this.accountService.getAllUserAccountByPage(page)
      .subscribe({
        next: (accountPagination: AccountPagination) => {
          this.pageWithContent[page - 1] = accountPagination.pagination?.content!
          this.last = accountPagination.pagination?.last
          this.totalElements = accountPagination.pagination?.totalElements
          this.totalPages = accountPagination.pagination?.totalPages
          this.numberOfElements = accountPagination.pagination?.numberOfElements
          console.log(this.pageWithContent)
        },
        error: (e) => {
          this.genericResponse.httpStatus = e.error.httpStatus
          this.genericResponse.message = e.error.message
          this.genericResponse.timeStamp = e.error.timeStamp
        },
        complete: () => {
          console.log("Last Page: " + this.last);
          console.log("Total Elements: " + this.totalElements)
          console.log("Total Pages: " + this.totalPages)
        }
      })
  }
}
