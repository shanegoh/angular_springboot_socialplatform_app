import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { SafeResourceUrl } from '@angular/platform-browser';
import { StreamService } from '../_services/stream.service';
import { FileResponse } from '../_models/response/file-response';
import { JWTService } from '../_services/jwt.service';
import { Content } from '../_models/content';

@Component({
  selector: 'toast',
  templateUrl: './toast.component.html',
  styleUrls: ['./toast.component.css']
})


export class ToastComponent implements OnInit {

  hyperLinkUrl: SafeResourceUrl | undefined;
  mediaLinkUrl: SafeResourceUrl | undefined;

  // get content from parent component (feed)
  @Input() content: Content | undefined

  // Send post id to (feed) component
  @Output() throwPostIdToFeed = new EventEmitter<number>();

  constructor(public jwtService: JWTService, private streamService: StreamService) { }

  ngOnInit(): void {
    this.loadMedia(this.content?.mediaLink!)
    this.hyperLinkUrl = this.streamService.checkYoutubeLink(this.content?.hyperLink!)
  }

  loadMedia(url: string) {
    console.log("loading media")
    this.streamService.streamMedia(url)
      .subscribe({
        next: (fileResponse: FileResponse) => {
          var file = "data:" + fileResponse.format + ";base64," + fileResponse.file
          this.mediaLinkUrl = this.streamService.sanitizerBypass(file);
        },
        error: (e) => {
          console.log(e.error)
        },
        complete: () => { }
      })
  }

  // Send to (feed) component to send to individual post 
  viewPost() {
    this.throwPostIdToFeed.emit(this.content?.id)
  }
}
