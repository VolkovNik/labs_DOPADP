package ru.bmstu.javascript.tester;

import akka.actor.AbstractActor;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ActorExecutor extends AbstractActor {

    public String executeTest(RequestBody requestBody) throws ScriptException, NoSuchMethodException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval("var divideFn = function(a,b) { return a/b}");
        Invocable invocable = (Invocable) engine;
        Object[] params = {2, 1};
        return (invocable.invokeFunction("divideFn", params).toString());
    }

    @Override
    public Receive createReceive() {
        return null;
    }
}
