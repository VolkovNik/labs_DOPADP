package ru.bmstu.javascript.tester.messages;

import java.util.ArrayList;

public class ReturnResultMsg {
    private final String packageId;
    private final ArrayList<String> result;

    public ReturnResultMsg(String packageId, ArrayList<String> result) {
        this.packageId = packageId;
        this.result = result;
    }

    public String getPackageId() {
        return packageId;
    }

    public ArrayList<String> getResult() {
        return result;
    }
}
