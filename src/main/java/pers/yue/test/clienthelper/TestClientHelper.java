/**
 * Created by zhangyue182 on 2017/11/23
 */
package pers.yue.test.clienthelper;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import pers.yue.exceptions.runtime.HttpTestRunException;
import pers.yue.test.clienthelper.error.TestError;
import pers.yue.test.clienthelper.logging.Banner;
import pers.yue.common.util.ThreadUtil;
import pers.yue.test.testcase.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static pers.yue.test.clienthelper.error.IgnoreAnyError.IgnoreAnyError;

/**
 * The abstract test client helper class.
 * This class defines shared functions for the use of test.
 */
public abstract class TestClientHelper {
    private static Logger logger = LoggerFactory.getLogger(ThreadUtil.getClassName());

    private Integer expectedStatusCode = null;
    private TestError expectedError = null;
    private String expectedExceptionType = "";
    private String expectedMessagePattern = "";

    @FunctionalInterface
    public interface SdkVoidMethodWrapper <P> {
        void execute(P param);
    }

    @FunctionalInterface
    public interface SdkVoidMethodWrapper2 <P1, P2> {
        void execute(P1 param1, P2 param2);
    }

    @FunctionalInterface
    public interface SdkMethodWrapper <P, R> {
        R execute(P param);
    }

    @FunctionalInterface
    public interface SdkMethodWrapper2 <P1, P2, R> {
        R execute(P1 param1, P2 param2);
    }

    @FunctionalInterface
    public interface SdkMethodOutputHandler<R> {
        void print(R result);
    }

    protected <P> void executeAndAssert(
            SdkVoidMethodWrapper<P> wrapper,
            P param,
            Banner banner
    ) {
        banner.print();

        try {
            wrapper.execute(param);
            assertSuccessExpected();
        } catch (RuntimeException e) {
            assertExpectedError(e);
        }
    }

    protected <P1, P2> void executeAndAssert(
            SdkVoidMethodWrapper2<P1, P2> wrapper,
            P1 param1,
            P2 param2,
            Banner banner
    ) {
        banner.print();

        try {
            wrapper.execute(param1, param2);
            assertSuccessExpected();
        } catch (RuntimeException e) {
            assertExpectedError(e);
        }
    }

    protected <P, R> R executeAndAssertWithResult(
            SdkMethodWrapper<P, R> wrapper,
            P param,
            Banner banner,
            SdkMethodOutputHandler<R> outputHandler
    ) {
        banner.print();

        R result = null;
        try {
            result = wrapper.execute(param);
            if(outputHandler != null) outputHandler.print(result);
            assertSuccessExpected();
        } catch (RuntimeException e) {
            assertExpectedError(e);
        }

        return result;
    }

    protected <P1, P2, R> R executeAndAssertWithResult(
            SdkMethodWrapper2<P1, P2, R> wrapper,
            P1 param1,
            P2 param2,
            Banner banner,
            SdkMethodOutputHandler<R> outputHandler
    ) {
        banner.print();

        R result = null;
        try {
            result = wrapper.execute(param1, param2);
            if(outputHandler != null) outputHandler.print(result);
            assertSuccessExpected();
        } catch (RuntimeException e) {
            assertExpectedError(e);
        }

        return result;
    }

    /**
     * Expects a specific error of the next call.
     * if no error or an error with different exception type or message pattern is caught, client helper will throws
     * an exception to let the test fail.
     * @param exceptionType
     * @param messagePattern
     * @return
     */
    protected TestClientHelper withExpectedError(String exceptionType, String messagePattern) {
        expectedExceptionType = exceptionType;
        expectedMessagePattern = messagePattern;
        clearExpectedHttpCode();
        clearExpectedError();
        return this;
    }

    /**
     * Expects a specific HTTP code of the next call.
     * If no error or an error with different HTTP code is caught, client helper will throws an exception to let the
     * test fail.
     * @param httpCode
     * @return
     */
    protected TestClientHelper withExpectedError(int httpCode) {
        expectedStatusCode = httpCode;
        clearExpectedError();
        clearExpectedException();
        return this;
    }

    /**
     * Expects a specific error of the next call.
     * If no error or a different error is caught, client helper will throws an exception to let the test fail.
     * @param error
     * @return
     */
    protected TestClientHelper withExpectedError(TestError error) {
        expectedError = error;
        clearExpectedHttpCode();
        clearExpectedException();
        return this;
    }

    /**
     * Ignores any error of the next call.
     * @return
     */
    protected TestClientHelper ignoreAnyError() {
        return withExpectedError(IgnoreAnyError.IgnoreAnyError.IgnoreAnyError);
    }

    public TestClientHelper clearExpectedException() {
        expectedExceptionType = "";
        expectedMessagePattern = "";
        return this;
    }

    private TestClientHelper clearExpectedHttpCode() {
        expectedStatusCode = null;
        return this;
    }

    public TestClientHelper clearExpectedError() {
        expectedError = null;
        return this;
    }

    public String getExpectedExceptionType() {
        return expectedExceptionType;
    }

    public String getExpectedMessagePattern() {
        return expectedMessagePattern;
    }

    public Integer getExpectedStatusCode() {
        return expectedStatusCode;
    }

    public TestError getExpectedError() {
        return expectedError;
    }

    private boolean isErrorExpected() {
        if(getExpectedError() == IgnoreAnyError.IgnoreAnyError.IgnoreAnyError) {
            // Any error is ignored.
            return false;
        } else {
            return (getExpectedStatusCode() != null || getExpectedError() != null || !getExpectedExceptionType().isEmpty());
        }
    }

    public void assertSuccessExpected() {
        try {
            if (isErrorExpected()) {
                TestCase.fail("Operation succeeded while failure expected.");
            }
        } finally {
            clearExpectedHttpCode();
            clearExpectedError();
            clearExpectedException();
        }
    }

    protected void assertExpectedError(Exception e) {
        if(e.getStackTrace().length > 25) {
            // Limit stack trace level to 25 to save log space.
            e.setStackTrace(Arrays.copyOf(e.getStackTrace(), 25));
        }

        try {
            if(getExpectedStatusCode() == null && getExpectedError() == null && getExpectedExceptionType().isEmpty()) {
                TestCase.fail(e.getMessage(), e);
            }

            if(getExpectedError() == IgnoreAnyError.IgnoreAnyError.IgnoreAnyError) {
                logger.info("Exception:", e);
                logger.info("Expected any error. This exception is ignored.");
                return;
            }

            if(getExpectedStatusCode() != null) {
                if (!isExpectedStatusCode(e, getExpectedStatusCode())){
                    TestCase.fail("Operation failed with un-expected HTTP code. Expected "
                            + getExpectedStatusCode() + " but got:", e);
                }
            }

            if(getExpectedError() != null) {
                if (!isExpectedError(e, getExpectedError())) {
                    TestCase.fail("Operation failed with un-expected error. Expected "
                            + getExpectedError().getHttpCode() + " "
                            + getExpectedError().getErrorCode() + " "
                            + getExpectedError().getDescription() + " but got:", e);
                }
            }

            if(getExpectedExceptionType() != null && !getExpectedExceptionType().isEmpty()) {
                if (!isExpectedExceptionTypeAndMatchMessagePattern(e, getExpectedExceptionType(), getExpectedMessagePattern())) {
                    TestCase.fail("Operation failed with un-expected exception. Expected "
                            + getExpectedExceptionType() + " "
                            + getExpectedMessagePattern() + " but got:", e);
                }
            }
        } finally {
            clearExpectedHttpCode();
            clearExpectedError();
            clearExpectedException();
        }
    }

    protected boolean isExpectedStatusCode(Exception e, Integer expectedStatusCode){
        boolean ret = false;
        if (e instanceof AmazonS3Exception) {
            if(((AmazonS3Exception) e).getStatusCode() == expectedStatusCode) {
                logger.info("Exception:", e);
                logger.info("Expected exception / HTTP code {}.", expectedStatusCode);
                ret = true;
            }
        } else if (e instanceof HttpTestRunException) {
            if(((HttpTestRunException) e).getHttpCode() == expectedStatusCode) {
                logger.info("Exception:", e);
                logger.info("Expected exception / HTTP code {}.", expectedStatusCode);
                ret = true;
            }
        } else {
            if (e.getMessage().contains(String.valueOf(getExpectedStatusCode()))) {
                logger.info("Exception:", e);
                logger.info("Expected exception / HTTP code {}.", getExpectedStatusCode());
                ret = true;
            }
        }
        return ret;
    }

    protected boolean isExpectedError(Exception e, TestError expectedError){
        boolean ret = false;
        if (e.getMessage().contains(String.valueOf(expectedError.getHttpCode()))
                && e.getMessage().contains(expectedError.getErrorCode())
                && e.getMessage().trim().replaceAll("\n", " ").matches(expectedError.getDescription())) {
            logger.info("Exception:", e);
            logger.info("Expected error: {} {} \"{}\".",
                    expectedError.getHttpCode(),
                    expectedError.getErrorCode(),
                    expectedError.getDescription());

            ret = true;
        }
        return ret;
    }

    protected boolean isExpectedExceptionTypeAndMatchMessagePattern(Exception e, String expectedExceptionType, String expectedMessagePattern){
        boolean ret = false;
        if (e.getClass().getSimpleName().contains(expectedExceptionType)
                && e.getMessage().trim().replaceAll("\n", " ").matches(expectedMessagePattern)) {
            logger.info("Exception:", e);
            logger.info("Expected exception: {} {}.",
                    expectedExceptionType,
                    expectedMessagePattern);
            ret = true;
        }
        return ret;
    }
}
