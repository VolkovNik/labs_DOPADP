package ru.bmstu.javascript.tester;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestBody {
    @JsonProperty("packageId")
    private String packageId;
    
}
