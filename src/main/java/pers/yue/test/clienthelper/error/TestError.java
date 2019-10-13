package pers.yue.test.clienthelper.error;

public interface TestError {
    String getErrorCode();

    String getDescription();

    int getHttpCode();
}