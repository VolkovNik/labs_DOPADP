package ru.bmstu.javascript.tester.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class TestData {
    private final String FIELD_TEST_NAME = "testName";
    private final String FIELD_EXPECTED_RESULT = "expectedResult";
    private final String FIELD_PARAMS = "params";

    @JsonProperty(FIELD_TEST_NAME)
    private final String testName;
    @JsonProperty(FIELD_EXPECTED_RESULT)
    private final String expectedResult;
    @JsonProperty(FIELD_PARAMS)
    private final ArrayList<String> params;

    public TestData (
            @JsonProperty(FIELD_TEST_NAME) String testName,
            @JsonProperty(FIELD_EXPECTED_RESULT) String expectedResult,
            @JsonProperty(FIELD_PARAMS) ArrayList<String> params) {
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
