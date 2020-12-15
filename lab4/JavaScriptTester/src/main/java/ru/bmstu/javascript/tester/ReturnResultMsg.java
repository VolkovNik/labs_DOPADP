package ru.bmstu.javascript.tester;

public class ReturnResultMsg {
    private final String packageId;
    private final String result;

    public ReturnResultMsg(String packageId, String result) {
        this.packageId = packageId;
        this.result = result;
    }

    public String getPackageId() {
        return packageId;
    }

    public String getResult() {
        return result;
    }
}
