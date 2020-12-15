package ru.bmstu.javascript.tester;

import java.util.ArrayList;

public class TestData {
    private final String packageId;
    private final String jsScript;
    private final String functionName;
    private final String testName;
    private final String expectedResult;
    private final ArrayList<String> params;

    public TestData (String packageId,
                     String jsScript,
                     String functionName,
                     String testName,
                     String expectedResult,
                     ArrayList<String> params) {
        this.packageId = packageId;
        this.jsScript = jsScript;
        this.functionName = functionName;
        this.testName = testName;
        this.expectedResult = expectedResult;
        this.params = params;
    }

    public String getPackageId() {
        return packageId;
    }

    public String getJsScript() {
        return jsScript;
    }

    public String getFunctionName() {
        return functionName;
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
