import { Content } from "./content";
import { GenericResponse } from "./generic-response";

export class PostResponse extends GenericResponse{
    postObject: Content | undefined
}
