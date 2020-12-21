package ru.bmstu.stress.testing.Messages;

public class StoreResultMsg {
    private final String testUrl;
    private final Integer result;

    public StoreResultMsg(String testUrl, Integer result) {
        this.testUrl = testUrl;
        this.result = result;
    }

    public Integer getResult() {
        return result;
    }

    public String getTestUrl() {
        return testUrl;
    }
}


