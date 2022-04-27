import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'popup',
  templateUrl: './popup.component.html',
  styleUrls: ['./popup.component.css']
})
export class PopupComponent implements OnInit {
  display = "none";

  @Input() showPopUp = false
  @Input() header = ""
  @Input() message = ""

  @Output() newItemEvent = new EventEmitter<boolean>();

  ngOnInit(): void {
  }

  constructor() {
    this.openModal();
  }

  openModal() {
    this.display = "block";
  }

  onCloseHandled() {
    this.display = "none";
    this.newItemEvent.emit(true); // update parent
  }
}
