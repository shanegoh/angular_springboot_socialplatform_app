import { Content } from "../content"

export class PostPaginationDetail {
    content: Content[] | undefined
    last: boolean | undefined
    totalElements: number | undefined
    totalPages: number | undefined
}
