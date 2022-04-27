import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RegistrationResponse } from '../_models/response/registration-response';
import { User } from '../_models/user';
import { RegistrationService } from '../_services/registration.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  user: User = new User()

  registrationResponse: RegistrationResponse = new RegistrationResponse()

  confirmPasswordErrorMessage: string | undefined

  showPopUpMessage: boolean | undefined

  registerHeaderMsg: string = "Success!"
  registerMsg: string = "Your account has been successfully created."

  constructor(private router: Router, private registrationService: RegistrationService) { }

  ngOnInit(): void {
  }

  getDisplayedStatus(status: boolean) {
    if(status) {  // if status is true -> means modal is closed. Proceed to login
        this.router.navigate(['/login'])
    }
  }

  // subscribe for creation account status from register method
  onSubmitRegister() {
    // Check for user object
    if (this.user) {
      if (this.user.confirmPassword !== this.user.password) { // compare password
        this.confirmPasswordErrorMessage = "Password does not match"
      } else {
        this.registrationService.register(this.user)
          .subscribe({
            next: (registrationResponse: RegistrationResponse) => {
              this.registrationResponse = registrationResponse
              this.showPopUpMessage = true
            },
            error: (e) => {
              this.registrationResponse.httpStatus = e.error.httpStatus
              this.registrationResponse.message = e.error.message
              this.registrationResponse.timeStamp = e.error.timeStamp
              this.registrationResponse.errorMessages = e.error.errorMessages
            },
            complete: () => console.log(this.registrationResponse)
          })
      }
    }
  }
}
