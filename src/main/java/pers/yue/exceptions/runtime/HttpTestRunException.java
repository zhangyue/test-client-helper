package pers.yue.exceptions.runtime;

import com.jcloud.util.test.exception.TestRunException;

/**
 * Created by zhangyue182 on 2019/05/10
 */
public class HttpTestRunException extends TestRunException {
    private int httpCode;
    private String statusLine;

    public HttpTestRunException(int httpCode, String statusLine, String message) {
        super(
                message.contains("Status line")? message
                        : "Http code: " + httpCode + ", Status line: " + statusLine + ", Message: " + message
        );
        this.httpCode = httpCode;
        this.statusLine = statusLine;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getStatusLine() {
        return statusLine;
    }

    public void setStatusLine(String statusLine) {
        this.statusLine = statusLine;
    }
}
