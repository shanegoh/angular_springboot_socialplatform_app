import { Content } from "./content";
import { GenericResponse } from "./generic-response";
import { PaginationDetail } from "./pagination-detail";

export class PostPagination extends GenericResponse{
    pagination: PaginationDetail | undefined
}
