import { Component, Input, OnDestroy, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.css']
})
export class NotificationComponent implements OnInit, OnDestroy {

  constructor() { }

  @Input() message: string | undefined

  @Output() onCloseNotification = new EventEmitter<boolean>();

  ngOnInit() {
    // set display 3000 miliseconds
    setInterval(()=> this.ngOnDestroy(), 3000);
  }

  // On destroy, notify the feed component and update show notification to false
  ngOnDestroy(): void {
    this.onCloseNotification.emit(true)
    console.log("destroyed")
  }
}
