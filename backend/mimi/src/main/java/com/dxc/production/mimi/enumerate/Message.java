package com.dxc.production.mimi.enumerate;

public enum Message {
    USERNAME_INVALID("Only digit, underscore and alphabets allowed"),
    USERNAME_EXIST("Username already exist"),
    EMAIL_INVALID("Invalid email address"),
    EMAIL_EXIST("Email already exist"),
    NAME_INVALID("Only alphabets allowed"),
    PASSWORD_INVALID("Password does not match the requirements"),
    EMPTY_FIELDS("Please ensure that all fields are filled");

    private String message;
    Message(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
    public static Message typeToMedia(String message) {
        for(Message r: Message.values()) {
            if(r.message.equals(message)) {
                return r;
            }
        }
        throw new IllegalArgumentException("No constant with message " + message + " found");
    }
}
