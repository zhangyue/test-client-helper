package pers.yue.exceptions.runtime;

import org.testng.TestNGException;

/**
 * Created by zhangyue182 on 05/16/2018
 */
public class TestConfigException extends TestNGException {
    public TestConfigException(Throwable t) {
        super(t);
    }

    public TestConfigException(String message, Throwable t) {
        super(message, t);
    }

    public TestConfigException(String message) {
        super(message);
    }
}
