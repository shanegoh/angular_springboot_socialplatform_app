import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { FileResponse } from '../_models/response/file-response';

@Injectable({
  providedIn: 'root'
})
export class StreamService {

  private baseURL = "http://localhost:8080"

  constructor(public sanitizer: DomSanitizer, private http: HttpClient) { }


  checkYoutubeLink(url: string) {
    if (url !== undefined && /youtube/i.test(url)) {
      url = url.replace("watch?v=", "embed/")
    }
    return this.sanitizerBypass(url); 
  }

  sanitizerBypass = (file: string): SafeResourceUrl => {
    return this.sanitizer.bypassSecurityTrustResourceUrl(file); 
  }

  streamMedia(url: string) {
    console.log("requesting for media")
    return this.http.get<FileResponse>(this.baseURL + `/api/post/stream/${url}`)
  }
  
}
