import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NAVComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void {}

  clearStorage(value: boolean) {
    if(value) {
      this.router.navigate(['/login'])
    }
  }
}
