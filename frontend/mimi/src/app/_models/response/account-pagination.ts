import { AccountPaginationDetail } from "./account-pagination-detail";
import { GenericResponse } from "./generic-response";

export class AccountPagination extends GenericResponse{
    pagination: AccountPaginationDetail | undefined
}
