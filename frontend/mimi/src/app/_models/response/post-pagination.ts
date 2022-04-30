import { GenericResponse } from "./generic-response";
import { PostPaginationDetail } from "./post-pagination-detail";

export class PostPagination extends GenericResponse{
    pagination: PostPaginationDetail | undefined
}
