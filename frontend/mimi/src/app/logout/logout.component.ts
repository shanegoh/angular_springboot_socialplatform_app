import { Component, EventEmitter, OnInit, Output } from '@angular/core';

@Component({
  selector: 'logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent implements OnInit {

  @Output() newItemEvent = new EventEmitter<boolean>();

  constructor() { }

  ngOnInit(): void {
  }

  onClickLogout() {
    localStorage.clear();
    this.newItemEvent.emit(true);
  }
}
