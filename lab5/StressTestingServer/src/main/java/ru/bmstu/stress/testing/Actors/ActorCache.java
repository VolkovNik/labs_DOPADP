package ru.bmstu.stress.testing.Actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import ru.bmstu.stress.testing.Messages.FindResultMsg;
import ru.bmstu.stress.testing.Messages.StoreResultMsg;

import java.util.HashMap;

public class ActorCache extends AbstractActor {
    public final Integer CODE_NOT_FOUND = -1;

    private final HashMap<String, Integer> testResults = new HashMap<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(FindResultMsg.class, msg -> {
                    String testUrl = msg.getTestUrl();
                    Integer result = testResults.get(testUrl);
                    if (result != null) {
                        getSender().tell(result, ActorRef.noSender());
                    } else {
                        getSender().tell(CODE_NOT_FOUND, ActorRef.noSender());
                    }
                })
                .match(StoreResultMsg.class, msg -> {
                    testResults.put(msg.getTestUrl(), msg.getResult());
                })
                .build();
    }
}
