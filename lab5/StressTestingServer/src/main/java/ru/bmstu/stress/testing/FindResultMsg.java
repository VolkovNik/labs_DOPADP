package ru.bmstu.stress.testing;

public class FindResultMsg {
    private final String testUrl;

    public FindResultMsg(String testUrl) {
        this.testUrl = testUrl;
    }

    public String getTestUrl() {
        return testUrl;
    }
}
