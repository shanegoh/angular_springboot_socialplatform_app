import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { SafeResourceUrl } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Content } from '../_models/response/content';
import { FileResponse } from '../_models/response/file-response';
import { PostResponse } from '../_models/response/post-response';
import { PostService } from '../_services/post.service';
import { StreamService } from '../_services/stream.service';

@Component({
  selector: 'individual-post',
  templateUrl: './individual-post.component.html',
  styleUrls: ['./individual-post.component.css']
})
export class IndividualPostComponent implements OnInit {

  constructor(private route: ActivatedRoute, private postService: PostService, private streamService: StreamService, private router:Router) { }

  postResponse: PostResponse = new PostResponse();
  content: Content | undefined

  hyperLinkUrl: SafeResourceUrl | undefined;
  mediaLinkUrl: SafeResourceUrl | undefined;

  @Input() individualPostId: number | undefined
  
  @Output() destroyPostComponent = new EventEmitter<boolean>();

  ngOnInit(): void {
    this.loadOnePost(this.individualPostId!)
  }

  loadOnePost(id: number) {
    this.postService.getOnePost(this.individualPostId!)
      .subscribe({
        next: (postResponse: PostResponse) => {
          this.content = postResponse.postObject
          console.log(postResponse)
          this.loadMedia(this.content?.mediaLink!)
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
    console.log("loading media")
    this.streamService.streamMedia(url)
      .subscribe({
        next: (fileResponse: FileResponse) => {
          var file = "data:" + fileResponse.format + ";base64," + fileResponse.file
          this.mediaLinkUrl = this.streamService.sanitizerBypass(file);
          console.log( this.mediaLinkUrl)
        },
        error: (e) => {
          console.log(e.error)
        },
        complete: () => { }
      })
  }

  onClickClosePost() {
    console.log("clicked closed")
    this.destroyPostComponent.emit(true)
  }
}
