package ru.bmstu.javascript.tester;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestBody {
    @JsonProperty("packageId")
    private String packageId;
    @JsonProperty("jsScript")
    private String jsScript;
    @JsonProperty("functionName")
    private String functionName;
    @JsonProperty("tests")
    private String tests;

    @JsonCreator
    public RequestBody(@JsonProperty("packageId")
                           String packageId
                                   @JsonProperty("jsScript")
    String jsScript
    @JsonProperty("functionName")
    String functionName
    @JsonProperty("tests")
    String tests)

}
