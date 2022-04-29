import { GenericResponse } from "./generic-response";

export class FileResponse extends GenericResponse{
    file: string | undefined
    format: string | undefined
}
