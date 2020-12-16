package ru.bmstu.javascript.tester.messages;

public class GetResultMsg {
    private final String packageId;

    public GetResultMsg(String packageId) {
        this.packageId = packageId;
    }

    public String getPackageId() {
        return packageId;
    }
}
