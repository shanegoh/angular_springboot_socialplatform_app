import { Content } from "./content"

export class PaginationDetail {
    content: Content[] | undefined
    last: boolean | undefined
    totalElements: number | undefined
    totalPages: number | undefined
}
