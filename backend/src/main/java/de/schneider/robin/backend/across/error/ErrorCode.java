package de.schneider.robin.backend.across.error;

public enum ErrorCode {

    PERSON_NOT_FOUND("100"),
    SECURITY_USER_NOT_FOUND("101");

    private final String errorCode;

    ErrorCode(String code) {
        this.errorCode = code;
    }

    public String getErrorCode() {
        return this.errorCode;
    }
}
