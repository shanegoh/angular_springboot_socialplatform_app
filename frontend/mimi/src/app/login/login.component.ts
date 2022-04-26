import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationResponse } from '../authentication-response';
import { AuthenticationService } from '../authentication.service';
import { User } from '../user';

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  user: User = new User();

  constructor(
    private authenticationService: AuthenticationService,
    private router: Router) {
  }

  ngOnInit(): void {}

  // subscribe for json web token by calling AuthenticationService login method
  onSubmitLogin() {
    if (this.user.username && this.user.password) {
      this.authenticationService.login(this.user.username, this.user.password)
        .subscribe({
          next: (authenticationResponse) => {
            localStorage.setItem('token', authenticationResponse.jsonWebToken!)
          },
          error: (e) => console.error(e.error.message),
          complete: () => console.log(localStorage.getItem('token'))
        })
    }
  }
}