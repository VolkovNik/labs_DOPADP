package ru.bmstu.javascript.tester;

import java.util.ArrayList;

public class TestData {
    private final String testName;
    private final String expectedResult;
    private final ArrayList<String> params;

    public TestData (
                     String testName,
                     String expectedResult,
                     ArrayList<String> params) {
        this.testName = testName;
        this.expectedResult = expectedResult;
        this.params = params;
    }
    
    public ArrayList<String> getParams() {
        return params;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public String getTestName() {
        return testName;
    }

}
