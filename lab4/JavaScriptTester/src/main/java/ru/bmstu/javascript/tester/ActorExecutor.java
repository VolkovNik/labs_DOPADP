package ru.bmstu.javascript.tester;

import akka.actor.AbstractActor;

public class ActorExecutor extends AbstractActor {
    @Override
    public Receive createReceive() {
        return null;
    }

    //        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
//        engine.eval("var divideFn = function(a,b) { return a/b}");
//        Invocable invocable = (Invocable) engine;
//        Object[] params = {2, 1};
//        System.out.println(invocable.invokeFunction("divideFn", params).toString());

}
