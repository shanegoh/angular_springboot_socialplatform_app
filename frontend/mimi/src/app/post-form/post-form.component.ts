import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { PostRequest } from '../_models/request/post-request';
import { GenericResponse } from '../_models/response/generic-response';
import { PostService } from '../_services/post.service';

@Component({
  selector: 'post-form',
  templateUrl: './post-form.component.html',
  styleUrls: ['./post-form.component.css']
})
export class PostFormComponent implements OnInit {

  genericResponse: GenericResponse = new GenericResponse()

  // data for sending over to the server
  postRequest: PostRequest = new PostRequest()
  fileToUpload: File | undefined;

  // shut the modal
  display = "none";

  // Outgoing data to inform parent (feed) to destroy this component after submit or close
  @Output() onClose = new EventEmitter<boolean>();

  constructor(private formBuilder: FormBuilder, private postService: PostService) { }

  ngOnInit() {
    this.openModal()
  }

  openModal() {
    this.display = "block";
  }

  // Close when user close form
  onCloseHandled() {
    this.display = "none";
    this.onClose.emit(true);
  }

  // Close when user submitted form
  onCloseSubmit() {
    this.display = "none";
    this.onClose.emit(true);
    this.onSubmitAttemptPost()
  }

  // trigger once uploaded file
  handleFileInput(event: Event) {
    const target = event.target as HTMLInputElement;
    const files = target.files as FileList;
    if (files) {
      console.log("FileUpload -> files", files[0]);
      this.fileToUpload = files[0]
    }
  }

  // Call post service to attempt to post content
  onSubmitAttemptPost() {
    const formData = new FormData();
    if(this.fileToUpload !== undefined) {
      formData.append('media', this.fileToUpload!)
    }
    formData.append('body', new Blob([JSON
      .stringify(this.postRequest)], {
      type: 'application/json'
    }));

    this.postService.postContent(formData)
      .subscribe({
        next: (genericResponse: GenericResponse) => {
          this.genericResponse = genericResponse
          console.log(genericResponse)
        },
        error: (e) => {
          this.genericResponse.httpStatus = e.error.httpStatus
          this.genericResponse.message = e.error.message
          this.genericResponse.timeStamp = e.error.timeStamp
          console.log(e.error)
        },
        complete: () => {}
      })
  }
}
