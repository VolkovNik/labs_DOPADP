package ru.bmstu.javascript.tester.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import ru.bmstu.javascript.tester.messages.*;

public class ActorExecutor extends AbstractActor {

    public String correctAnswer(TestDataMsg testDataMsg) {
        return testDataMsg.getTestName() + " from package " + testDataMsg.getPackageId()
                + " succeed with result " + testDataMsg.getExpectedResult();
    }

    public String wrongAnswer(TestDataMsg testDataMsg, String actorAnswer) {
        return testDataMsg.getTestName() + " from package " + testDataMsg.getPackageId()
                + " not succeed, expected: " + testDataMsg.getExpectedResult() + " but got: " + actorAnswer;
    }

    public String executeTest(TestDataMsg testDataMsg) throws ScriptException, NoSuchMethodException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(testDataMsg.getJsScript());
        Invocable invocable = (Invocable) engine;
        Object[] params = testDataMsg.getParams().toArray();
        String actorTestAnswer = (invocable.invokeFunction(testDataMsg.getFunctionName(), params).toString());
        if (actorTestAnswer.equals(testDataMsg.getExpectedResult())) {
            return correctAnswer(testDataMsg);
        } else {
            return wrongAnswer(testDataMsg, actorTestAnswer);
        }
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(TestDataMsg.class,
                        testDataMsg -> getSender().tell(
                                new StoreResultMsg(testDataMsg.getPackageId(), executeTest(testDataMsg)), ActorRef.noSender())
                        )
                .build();
    }
}