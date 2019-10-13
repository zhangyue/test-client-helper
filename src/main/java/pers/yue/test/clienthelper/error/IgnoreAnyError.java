package pers.yue.test.clienthelper.error;

/**
 * Created by Zhang Yue on 5/15/2019
 */
public enum IgnoreAnyError implements TestError {
    // With IgnoreAnyError, any error is ignored.
    IgnoreAnyError("IgnoreAnyError", ".*",
            200);

    private String errorCode;
    private String description;
    private int httpCode;

    public String getErrorCode() {
        return errorCode;
    }

    public String getDescription() {
        return description;
    }

    public int getHttpCode() {
        return httpCode;
    }

    IgnoreAnyError(String errorCode, String description, int httpCode) {
        this.errorCode = errorCode;
        this.description = description;
        this.httpCode = httpCode;
    }

    @Override
    public String toString() {
        return errorCode;
    }
}
