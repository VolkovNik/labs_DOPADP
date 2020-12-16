package ru.bmstu.javascript.tester;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;
import java.util.HashMap;

import ru.bmstu.javascript.tester.Messages.*;

public class ActorStorage extends AbstractActor {
    private final HashMap<String, ArrayList<String>> testResults = new HashMap<>();


    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(GetResultMsg.class, msg -> {
                    ArrayList<String> results = new ArrayList<>();
                    String id = msg.getPackageId();
                    ArrayList<String> result = testResults.get(id);
                    getSender().tell(new ReturnResultMsg(id, result), ActorRef.noSender());
                })
                .match(StoreResultMsg.class, msg -> {
                    if (!testResults.containsKey(msg.getPackageId())) {
                        testResults.put(msg.getPackageId(), new ArrayList<>());
                    }
                    testResults.get(msg.getPackageId()).add(msg.getTestResult());
                })
                .build();
    }
}
