package ru.bmstu.stress.testing;

public class ReturnResultMsg {
    private final String testUrl;
    private final Integer result;

    public ReturnResultMsg(String testUrl, Integer result) {
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


