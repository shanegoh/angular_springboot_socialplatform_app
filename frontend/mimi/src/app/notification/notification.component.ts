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

  interval: ReturnType<typeof setTimeout> | null | undefined

  ngOnInit() {
    // set display 3000 miliseconds
    this.interval = setInterval(() => 
      this.ngOnDestroy(), 1000);
  }

  // On destroy, notify the feed component and update show notification to false
  ngOnDestroy(): void {
    clearInterval(this.interval!)
    this.onCloseNotification.emit(true)
  }
}
