package edu.bbte.data.beim1992.backend.utils;

public enum HttpStatusCode {

    FORBIDDEN(403),
    NOT_FOUND(404),
    RATE_LIMIT_EXCEEDED(429),
    SERVICE_UNAVAILABLE(503),
    UNKNOWN(-1);

    private final int value;

    HttpStatusCode(final int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }

    public static HttpStatusCode getHttpStatusCode(int code) {

        switch (code) {

            case 403: return FORBIDDEN;
            case 404: return NOT_FOUND;
            case 429: return RATE_LIMIT_EXCEEDED;
            case 503: return SERVICE_UNAVAILABLE;
            default:  return UNKNOWN;
        }
    }
}
