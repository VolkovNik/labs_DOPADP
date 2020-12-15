package ru.bmstu.javascript.tester;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;
import java.util.HashMap;

public class ActorStorage extends AbstractActor {
    private final HashMap<String, ArrayList<String>> testResults = new HashMap<>();


    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(GetResultMsg.class, msg -> {
                    ArrayList<String> results = new ArrayList<>();
                    results.add("ololol");
                    testResults.put("2", results);
                    String id = msg.getPackageId();
                    ArrayList<String> result = testResults.get(id);

                    getSender().tell(new ReturnResultMsg(id, result), getSender());
                }).build();
    }
}
