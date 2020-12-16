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

    private final String JS_ENGINGE = "nashorn";
    private final String STR_FROM_PACKAGE = " from package ";
    private final String STR_SUCCEED_WIHT_RSLT = " succeed with result ";
    private final String STR_NOT_SUCCEED = " not succeed, expected: ";
    private final String STR_BUT_GOT = " but got: ";
    private final String STR_WITH_PARAMS = " with params: ";

    public String correctAnswer(TestDataMsg testDataMsg) {
        return testDataMsg.getTestName() + STR_FROM_PACKAGE + testDataMsg.getPackageId()
                + STR_SUCCEED_WIHT_RSLT + testDataMsg.getExpectedResult();
    }

    public String wrongAnswer(TestDataMsg testDataMsg, String actorAnswer) {
        return testDataMsg.getTestName() + STR_FROM_PACKAGE + testDataMsg.getPackageId()
                + STR_NOT_SUCCEED + testDataMsg.getExpectedResult() + STR_BUT_GOT + actorAnswer
                + STR_WITH_PARAMS + testDataMsg.getParams();
    }

    public String executeTest(TestDataMsg testDataMsg) throws ScriptException, NoSuchMethodException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName(JS_ENGINGE);
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
