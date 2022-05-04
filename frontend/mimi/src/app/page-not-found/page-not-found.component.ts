import { Component, OnInit  } from '@angular/core';


@Component({
  selector: 'page-not-found',
  templateUrl: './page-not-found.component.html',
  styleUrls: ['./page-not-found.component.css']
})
export class PageNotFoundComponent implements OnInit {
  location: any;

  constructor() { }

  ngOnInit(): void {
  }

  bringMeBack() {
    window.history.go(-1)
  }
}
