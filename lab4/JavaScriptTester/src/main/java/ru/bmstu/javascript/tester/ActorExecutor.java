package ru.bmstu.javascript.tester;

import akka.actor.AbstractActor;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ActorExecutor extends AbstractActor {

    public String executeTest(TestDataMsg testDataMsg) throws ScriptException, NoSuchMethodException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(testDataMsg.getJsScript());
        Invocable invocable = (Invocable) engine;
        Object[] params = testDataMsg.getParams().toArray();
        return (invocable.invokeFunction(requestBody.getFunctionName(), params).toString());
    }

    @Override
    public Receive createReceive() {
        return null;
    }
}
