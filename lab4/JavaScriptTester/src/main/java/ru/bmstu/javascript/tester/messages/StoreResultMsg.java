package ru.bmstu.javascript.tester.messages;

public class StoreResultMsg {
    private final String packageId;
    private final String testResult;

    public StoreResultMsg(String packageId, String testResult) {
        this.packageId = packageId;
        this.testResult = testResult;
    }

    public String getPackageId() {
        return packageId;
    }

    public String getTestResult() {
        return testResult;
    }
}

