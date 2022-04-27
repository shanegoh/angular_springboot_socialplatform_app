import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationResponse } from '../_models/response/authentication-response';
import { AuthenticationService } from '../_services/authentication.service';
import { User } from '../_models/user';
import { JWTService } from '../_services/jwt.service';
import { GenericResponse } from '../_models/response/generic-response';


@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  user: User = new User();

  genericResponse: GenericResponse = new GenericResponse();

  constructor(
    private authenticationService: AuthenticationService,
    private jwtService: JWTService,
    private router: Router) {
  }

  ngOnInit(): void {}

  // subscribe for jwt by calling AuthenticationService login method
  onSubmitLogin() {
    if (this.user.username && this.user.password) {
      this.authenticationService.login(this.user.username, this.user.password)
        .subscribe({
          next: (authenticationResponse: AuthenticationResponse) => {
            this.jwtService.setTokenDetails(authenticationResponse.jsonWebToken!) // set token details
            // redirect to page base on the role.
            this.router.navigate(['/userHome'])
          },
          error: (e) => console.error( this.genericResponse = e.error),
          complete: () => console.log("Role -> " + this.jwtService.getRole())
        })
    }
  }
}