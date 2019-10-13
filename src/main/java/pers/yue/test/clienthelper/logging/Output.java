package pers.yue.test.clienthelper.logging;

import com.jcloud.util.common.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Zhang Yue on 5/15/2019
 */
public class Output {
    private List<Map<String, String>> propertiesLines = new ArrayList<>();

    public Output(String outputKey, String outputValue) {
        Map<String, String> propertyLine = new TreeMap<>();
        propertyLine.put(outputKey, outputValue);

        this.propertiesLines.add(propertyLine);
    }

    public Output(Map<String, String> propertyLine) {
        this.propertiesLines.add(propertyLine);
    }

    public Output(List<Map<String, String>> propertyLine) {
        this.propertiesLines.addAll(propertyLine);
    }

    public void print() {
        LogUtil.printPropertiesLines(propertiesLines, "=");
    }
}