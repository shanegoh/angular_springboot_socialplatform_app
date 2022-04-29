import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Content } from '../_models/response/content';
import { GenericResponse } from '../_models/response/generic-response';
import { PostPagination } from '../_models/response/post-pagination';
import { PostService } from '../_services/post.service';

@Component({
  selector: 'feed',
  templateUrl: './feed.component.html',
  styleUrls: ['./feed.component.css']
})

export class FeedComponent implements OnInit {

  pageNumber: number = 0;
  contentList: Content[] = []
  last: boolean | undefined
  totalElements: number | undefined
  totalPages: number | undefined
  genericResponse: GenericResponse = new GenericResponse();

  // Data used by the ngIf to display the child component (postForm)
  openPostForm = false; 

  constructor(private postService: PostService) { }

  ngOnInit(): void {
    this.getPaginationData()
  }

  // subscribe for creation account status from register method
  onClickLoadNextPage(event: MouseEvent) {
    // Disable button on click on the final page
    if(this.totalPages !== undefined && this.pageNumber >= this.totalPages - 1)
      (event.target as HTMLButtonElement).hidden = true;

    this.getPaginationData()
  }

  getPaginationData() {
    this.pageNumber = this.pageNumber + 1;
    this.postService.getPostByPage(this.pageNumber)
    .subscribe({
      next: (postPagination: PostPagination) => {
        this.contentList.push(...postPagination.pagination?.content!) 
        this.last = postPagination.pagination?.last
        this.totalElements = postPagination.pagination?.totalElements
        this.totalPages = postPagination.pagination?.totalPages
      },
      error: (e) => {               
        this.genericResponse.httpStatus = e.error.httpStatus
        this.genericResponse.message = e.error.message
        this.genericResponse.timeStamp = e.error.timeStamp
      },
      complete: () => {
        console.log(this.contentList); 
        console.log("Last Page: " + this.last); 
        console.log("Total Elements: " + this.totalElements)
        console.log("Total Pages: " + this.totalPages)
      }
    })
  }

  // this method get called by (addPost) component to trigger
  // the display of post form
  updatePostFormStatus(value: boolean) {
    this.openPostForm = true;
  }

  // this method get called by (postForm) component to 
  // destroy the component
  closePostForm(value: boolean) {
    this.openPostForm = false;
  }
}
