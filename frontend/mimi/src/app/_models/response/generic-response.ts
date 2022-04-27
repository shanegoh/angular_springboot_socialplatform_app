import { GenericResponseInterface } from "./generic-response-interface";

export class GenericResponse implements GenericResponseInterface{

    timeStamp: Date | undefined
    message: string | undefined
    httpStatus: string | undefined

    
    getMessage(): string {
        return this.message!
    }
    getHttpStatus(): string {
        return this.httpStatus!
    }
}
