import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Content } from '../_models/content';
import { PostRequest } from '../_models/request/post-request';
import { GenericResponse } from '../_models/response/generic-response';
import { PostService } from '../_services/post.service';

@Component({
  selector: 'update-form',
  templateUrl: './update-form.component.html',
  styleUrls: ['./update-form.component.css']
})
export class UpdateFormComponent implements OnInit, OnDestroy {

  myGroup: FormGroup | undefined

  fileToUpload: File | undefined;

  postRequest: PostRequest = new PostRequest()

  genericResponse: GenericResponse = new GenericResponse()

  individualContent: Content | undefined

  constructor(private formBuilder: FormBuilder, private postService: PostService) {
  }
  ngOnDestroy(): void {
    console.log(this.postContent)
  }

  @Input() postContent: Content | undefined

  @Output() onClose = new EventEmitter<boolean>()
  @Output() throwResponse = new EventEmitter<string>()


  ngOnInit(): void {
    // shallow copy to prevent updating parent component
    this.individualContent! = { ...this.postContent! }
    console.log(this.individualContent)
    this.myGroup = this.formBuilder.group({
      'caption': '',
      'hyperLink': '',
      'file': '',
    });
  }

  onClickUpdate() {
    const formData = new FormData();
    this.postRequest.caption = this.individualContent?.caption
    this.postRequest.hyperLink = this.individualContent?.hyperLink
    this.postRequest.id = this.individualContent?.id

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

  checkValid() {
    // Check if same as original content
    if ((this.myGroup?.get('file')!.value !== '') || (this.myGroup?.get('caption')!.value !== '' && this.myGroup?.get('caption')!.value !== this.postContent?.caption)
      || (this.myGroup?.get('hyperLink')!.value && this.myGroup?.get('hyperLink')!.value !== this.postContent?.hyperLink)) {
      if (this.myGroup?.get('caption')!.value !== '' || this.myGroup?.get('hyperLink')!.value !== '' ||
        this.myGroup?.get('caption')!.value !== null || this.myGroup?.get('hyperLink')!.value !== null) {
        return false;
      } else {
        return true
      }
    } else {
      return true;
    }
  }
}
