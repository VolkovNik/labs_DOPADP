package ru.bmstu.javascript.tester;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class JavaScriptTester {

    ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
    engine.eval(jscript);
    Invocable invocable = (Invocable) engine;
    return invocable.invokeFunction(functionName, params).toString();

}
