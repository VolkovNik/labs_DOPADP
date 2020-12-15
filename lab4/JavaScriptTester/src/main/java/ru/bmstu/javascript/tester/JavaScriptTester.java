package ru.bmstu.javascript.tester;

public class JavaScriptTester {

    ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
    engine.eval(jscript);
    Invocable invocable = (Invocable) engine;
    return invocable.invokeFunction(functionName, params).toString();

}
