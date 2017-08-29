package io.github.freuvim.sipregister.type;

/**
 * Created by matheus on 29/08/17.
 */

public enum HttpCodeType {

    OK(200),
    CREATED(201),
    ACCEPT(202),
    NO_CONTENT(204),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    ERROR(500);

    private int code;

    HttpCodeType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
