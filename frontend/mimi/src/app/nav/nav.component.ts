import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { JWTService } from '../_services/jwt.service';

@Component({
  selector: 'nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NAVComponent implements OnInit {

  constructor(private router: Router, private jwtService:JWTService) { }

  username: string | undefined | null

  ngOnInit(): void {
    this.username = this.jwtService.getUsername()
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['/login'])
  }
}
