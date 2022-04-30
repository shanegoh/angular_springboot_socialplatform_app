import { Account } from "../account"

export class AccountPaginationDetail {
    content: Account[] | undefined
    last: boolean | undefined
    totalElements: number | undefined
    totalPages: number | undefined
    numberOfElements: number | undefined
}
