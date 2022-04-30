import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GenericResponse } from '../_models/response/generic-response';
import { PostPagination } from '../_models/response/post-pagination';
import { PostResponse } from '../_models/response/post-response';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private baseURL = "http://localhost:8080"

  constructor(private http: HttpClient) { }

  getPostByPage(pageNumber: number) {
    return this.http.get<PostPagination>(`${this.baseURL}/api/post/user/getAllPost/page/${pageNumber}`)
  }

  getPostByPageAdmin(pageNumber: number) {
    return this.http.get<PostPagination>(`${this.baseURL}/api/post/admin/getAllPost/page/${pageNumber}`)
  }

  postContent(formData: FormData) {
    return this.http.post<GenericResponse>(`${this.baseURL}/api/post/user/postContent`, formData)
  }

  updateContent(formData: FormData) {
    return this.http.put<GenericResponse>(`${this.baseURL}/api/post/updatePost`, formData)
  }

  getOnePost(id: number) {
    return this.http.get<PostResponse>(`${this.baseURL}/api/post/getPost/${id}`)
  }

  deletePostById(id: number) {
    return this.http.delete<GenericResponse>(`${this.baseURL}/api/post/deletePost/${id}`)
  }
}
