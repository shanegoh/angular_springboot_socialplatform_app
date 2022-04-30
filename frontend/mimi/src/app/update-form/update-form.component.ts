import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Content } from '../_models/content';
import { PostRequest } from '../_models/request/post-request';
import { GenericResponse } from '../_models/response/generic-response';
import { PostService } from '../_services/post.service';

@Component({
  selector: 'update-form',
  templateUrl: './update-form.component.html',
  styleUrls: ['./update-form.component.css']
})
export class UpdateFormComponent implements OnInit {

  ngOnInit(): void { }

  myGroup: FormGroup

  fileToUpload: File | undefined;

  postRequest: PostRequest = new PostRequest()

  genericResponse: GenericResponse = new GenericResponse()

  constructor(private formBuilder: FormBuilder, private postService: PostService) {
    this.myGroup = this.formBuilder.group({
      'caption': undefined,
      'hyperLink': undefined,
      'file': undefined
    });
  }

  @Input() postContent: Content | undefined

  @Output() onClose = new EventEmitter<boolean>()
  @Output() throwResponse = new EventEmitter<string>()

  onClickUpdate() {
   
    const formData = new FormData();
    this.postRequest.caption = this.postContent?.caption
    this.postRequest.hyperLink = this.postContent?.hyperLink
    this.postRequest.id = this.postContent?.id
    
    if (this.fileToUpload !== undefined) {
      formData.append('media', this.fileToUpload!)
    }

    formData.append('body', new Blob([JSON
      .stringify(this.postRequest)], {
      type: 'application/json'
    }));

    this.postService.updateContent(formData)
      .subscribe({
        next: (genericResponse: GenericResponse) => {
          this.genericResponse = genericResponse
          console.log("set responseed")
        },
        error: (e) => {
          this.genericResponse.httpStatus = e.error.httpStatus
          this.genericResponse.message = e.error.message
          this.genericResponse.timeStamp = e.error.timeStamp
          console.log(e.error)
        },
        complete: () => {
          this.throwResponse.emit(this.genericResponse.message)
          this.onClose.emit(true);
        }
      })
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
}
