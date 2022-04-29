import { Component, EventEmitter, OnInit, Output } from '@angular/core';

@Component({
  selector: 'add-post',
  templateUrl: './add-post.component.html',
  styleUrls: ['./add-post.component.css']
})
export class AddPostComponent implements OnInit {

  @Output() updateFeedToShowForm = new EventEmitter<boolean>();
  
  constructor() { }

  ngOnInit(): void {}

  // When add button is clicked, update parent then to (postform)
  // to display form
  // add-post(child) -> feed (parent)-> post-form (child)
  onClickTriggerPostForm() {
    this.updateFeedToShowForm.emit(true);
  }
}
