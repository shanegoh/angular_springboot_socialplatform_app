import { Component, Input, OnInit, PipeTransform } from '@angular/core';
import { Content } from '../_models/response/content';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { StreamService } from '../_services/stream.service';
import { FileResponse } from '../_models/response/file-response';

@Component({
  selector: 'toast',
  templateUrl: './toast.component.html',
  styleUrls: ['./toast.component.css']
})


export class ToastComponent implements OnInit {
  hyperLinkUrl: SafeResourceUrl | undefined;
  mediaLinkUrl: SafeResourceUrl | undefined;


  @Input() content: Content | undefined;

  constructor(public sanitizer: DomSanitizer, private streamService: StreamService) { }

  ngOnInit(): void {
    // checker link https://picsum.photos/200/30
    var hyperLink = this.content?.hyperLink!;

    const reader = new FileReader();
    this.loadMedia(this.content?.mediaLink!)

    if (hyperLink !== undefined && /youtube/i.test(hyperLink)) {
      hyperLink = hyperLink.replace("watch?v=", "embed/")
    }

    this.hyperLinkUrl = this.sanitizerBypass(hyperLink); 
  }

  sanitizerBypass = (file: string) => {
    return this.sanitizer.bypassSecurityTrustResourceUrl(file); 
  }

  loadMedia(url: string) {
    console.log("loading media")
    this.streamService.streamMedia(url)
      .subscribe({
        next: (fileResponse:FileResponse) => {
          var file = "data:" + fileResponse.format + ";base64," + fileResponse.file
          this.mediaLinkUrl = this.sanitizer.bypassSecurityTrustResourceUrl(file);
        },
        error: (e) => {
          console.log(e.error)
        },
        complete: () => {}
      })
  }

  viewPost() {
    console.log(this.content?.id)
    console.log("CLICKED")
  }
}
