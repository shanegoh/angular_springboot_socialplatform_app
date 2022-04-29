import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'popup',
  templateUrl: './popup.component.html',
  styleUrls: ['./popup.component.css']
})
export class PopupComponent implements OnInit {
  display = "none";

  // Get data from parent (register) component
  @Input() showPopUp = false
  @Input() header = ""
  @Input() message = ""

  @Output() returnStatus = new EventEmitter<boolean>();

  ngOnInit(): void {
    this.openModal();
  }

  constructor() {}

  openModal() {
    this.display = "block";
  }

  onCloseHandled() {
    this.display = "none";
    this.returnStatus.emit(true); // update parent
  }
}
