import { Component, EventEmitter, OnInit, Output } from '@angular/core';

@Component({
  selector: 'refresh',
  templateUrl: './refresh.component.html',
  styleUrls: ['./refresh.component.css']
})
export class RefreshComponent implements OnInit {

  constructor() { }

  @Output() reload = new EventEmitter<boolean>();
  
  ngOnInit(): void {
  }

  // onclick reload feed
  onClickRefresh() {
    this.reload.emit(true);
  }

}
