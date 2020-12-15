package ru.bmstu.javascript.tester;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JavaScriptTester {
    public static void main(String[] args) throws ScriptException {

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval("var divideFn = function(a,b) { return a/b}");
        Invocable invocable = (Invocable) engine;

    }
}
