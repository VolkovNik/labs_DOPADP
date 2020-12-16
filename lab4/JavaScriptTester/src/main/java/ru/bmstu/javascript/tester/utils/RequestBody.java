package ru.bmstu.javascript.tester.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class RequestBody {
    private final String FIELD_PACKAGE_ID = "packageId";
    private final String FIELD_JS_SCRIPT = "jsScript";
    private final String FIELD_FUNCTION_NAME = "functionName";
    private final String FIELD_TESTS = "tests";

    @JsonProperty(FIELD_PACKAGE_ID)
    private final String packageId;
    @JsonProperty(FIELD_JS_SCRIPT)
    private final String jsScript;
    @JsonProperty(FIELD_FUNCTION_NAME)
    private final String functionName;
    @JsonProperty(FIELD_TESTS)
    private final ArrayList<TestData> tests;

    @JsonCreator
    public RequestBody(@JsonProperty(FIELD_PACKAGE_ID) String packageId,
                       @JsonProperty(FIELD_JS_SCRIPT) String jsScript,
                       @JsonProperty(FIELD_FUNCTION_NAME) String functionName,
                       @JsonProperty(FIELD_TESTS) ArrayList<TestData> tests) {
        this.packageId = packageId;
        this.jsScript = jsScript;
        this.functionName = functionName;
        this.tests = tests;
    }

    public String getPackageId() {
        return packageId;
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getJsScript() {
        return jsScript;
    }

    public ArrayList<TestData> getTests() {
        return tests;
    }
}
