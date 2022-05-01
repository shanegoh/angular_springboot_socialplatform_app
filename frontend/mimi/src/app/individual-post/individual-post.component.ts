import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { SafeResourceUrl } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { FileResponse } from '../_models/response/file-response';
import { GenericResponse } from '../_models/response/generic-response';
import { PostResponse } from '../_models/response/post-response';
import { PostService } from '../_services/post.service';
import { StreamService } from '../_services/stream.service';
import { JWTService } from '../_services/jwt.service';
import { Content } from '../_models/content';

@Component({
  selector: 'individual-post',
  templateUrl: './individual-post.component.html',
  styleUrls: ['./individual-post.component.css']
})
export class IndividualPostComponent implements OnInit {

  onClickClosePost() {
    this.destroyPostComponent.emit(true)
  }

  constructor(private route: ActivatedRoute, 
    private postService: PostService, 
    private streamService: StreamService,
    public jwtService: JWTService) {}

  postResponse: PostResponse = new PostResponse();
  content: Content | undefined
  hyperLinkUrl: SafeResourceUrl | undefined;
  mediaLinkUrl: SafeResourceUrl | undefined;

  @Input() individualPostId: number | undefined

  @Output() destroyPostComponent = new EventEmitter<boolean>();

  // throw back to feed to display notification
  @Output() throwNotification = new EventEmitter<boolean>();
  @Output() throwResponse = new EventEmitter<string>();

  genericResponse: GenericResponse = new GenericResponse();

  openUpdateFormStatus = false;

  disableUpdateBtn = true;

  ngOnInit(): void {
    this.loadOnePost()
  }

  loadOnePost() {
    this.postService.getOnePost(this.individualPostId!)
      .subscribe({
        next: (postResponse: PostResponse) => {
          this.content = postResponse.postObject
          console.log(postResponse)
          if(this.content?.mediaLink !== null) {
            this.loadMedia(this.content?.mediaLink!)
          }
          this.hyperLinkUrl = this.streamService.checkYoutubeLink(this.content?.hyperLink!)
        },
        error: (e) => {
          this.postResponse.httpStatus = e.error.httpStatus
          this.postResponse.message = e.error.message
          this.postResponse.timeStamp = e.error.timeStamp
        },
        complete: () => { }
      })
  }

  loadMedia(url: string) {
    this.streamService.streamMedia(url)
      .subscribe({
        next: (fileResponse: FileResponse) => {
          var file = "data:" + fileResponse.format + ";base64," + fileResponse.file
          this.mediaLinkUrl = this.streamService.sanitizerBypass(file);
          console.log(this.mediaLinkUrl)
        },
        error: (e) => {
          console.log(e.error)
        },
        complete: () => { }
      })
  }

  deletePost() {
    console.log(this.content?.id)
    this.postService.deletePostById(this.content?.id!)
      .subscribe({
        next: (genericResponse: GenericResponse) => {
          this.genericResponse = genericResponse
          console.log(genericResponse)
          this.throwNotification.emit(true)
          this.throwResponse.emit(genericResponse.message)
          this.onClickClosePost()
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

  // set open form status
  openUpdateForm() {
    this.openUpdateFormStatus = true
  }

  closeUpdateForm() {
    this.openUpdateFormStatus = false
  }

  // status from update-form component
  closePost(value: boolean) {
    this.onClickClosePost()
  }

  // response from update-form component
  setResponse(value: string) {
    this.throwNotification.emit(true)
    this.throwResponse.emit(value)
  }
}
