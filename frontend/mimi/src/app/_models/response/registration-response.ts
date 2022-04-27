import { RegistrationError } from "../registration-error";
import { GenericResponse } from "./generic-response";

export class RegistrationResponse extends GenericResponse {

    errorMessages: RegistrationError | undefined;
}
