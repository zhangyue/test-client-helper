package pers.yue.test.clienthelper.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.yue.common.util.LogUtil;
import pers.yue.common.util.ThreadUtil;

/**
 * Created by Zhang Yue on 5/15/2019
 */
public class BasicBanner implements Banner {
    private static Logger logger = LoggerFactory.getLogger(ThreadUtil.getClassName());

    private String operation;
    private int logLevel = LogUtil.LOG_LEVEL_VV_FUNCTIONAL;

    public BasicBanner(String operation) {
        this.operation = operation;
    }

    public void print() {
        if (getLogLevel() >= LogUtil.LOG_LEVEL_V_LIGHT_STRESS) {
            logger.info("# [{}]", operation);
        }
    }

    public BasicBanner withOperation(String operation) {
        this.operation = operation;
        return this;
    }

    public String getOperation() {
        return operation;
    }

    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    public int getLogLevel() {
        return logLevel;
    }
}
