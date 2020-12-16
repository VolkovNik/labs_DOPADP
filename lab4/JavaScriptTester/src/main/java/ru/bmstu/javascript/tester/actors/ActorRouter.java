package ru.bmstu.javascript.tester.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import akka.routing.RoundRobinPool;

import java.util.ArrayList;

import ru.bmstu.javascript.tester.utils.RequestBody;
import ru.bmstu.javascript.tester.utils.TestData;
import ru.bmstu.javascript.tester.messages.*;

public class ActorRouter extends AbstractActor {

    private final ActorRef actorExecutor = getContext().actorOf(
            new RoundRobinPool(5).props(Props.create(ActorExecutor.class)));
    private final ActorRef actorStorage = getContext().actorOf(Props.create(ActorStorage.class));


    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(RequestBody.class,
                        msg -> {
                            ArrayList<TestData> testData = msg.getTests();
                            for (TestData test: testData) {
                                TestDataMsg testDataMsg = new TestDataMsg(
                                        msg.getPackageId(),
                                        msg.getJsScript(),
                                        msg.getFunctionName(),
                                        test.getTestName(),
                                        test.getExpectedResult(),
                                        test.getParams());
                                actorExecutor.tell(testDataMsg, actorStorage);
                            }

                })
                .match(GetResultMsg.class,
                        msg -> actorStorage.tell(msg, getSender())).build();

    }
}
