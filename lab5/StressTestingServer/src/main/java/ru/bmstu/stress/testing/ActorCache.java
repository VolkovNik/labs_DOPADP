package ru.bmstu.stress.testing;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;
import java.util.HashMap;

public class ActorCache extends AbstractActor {
    private final HashMap<String, Integer> testResults = new HashMap<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(FindResultMsg.class, msg -> {
                    String testUrl = msg.getTestUrl();
                    Integer result = testResults.get(testUrl);
                })
                .build();
    }
}
