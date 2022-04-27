import { GenericResponse } from "./generic-response";

export class AuthenticationResponse extends GenericResponse{
    jsonWebToken: string | undefined;
}
