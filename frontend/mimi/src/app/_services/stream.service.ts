import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FileResponse } from '../_models/response/file-response';

@Injectable({
  providedIn: 'root'
})
export class StreamService {

  private baseURL = "http://localhost:8080"

  constructor(private http: HttpClient) { }

  streamMedia(url: string) {
    console.log("requesting for media")
    return this.http.get<FileResponse>(this.baseURL + `/api/post/stream/${url}`)
  }
}
