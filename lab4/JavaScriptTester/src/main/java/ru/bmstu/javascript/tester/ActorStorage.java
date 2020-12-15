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
                .match(GetResultMsg.class,
                        msg -> {
                    String id = msg.getPackageId();
                    String result = testResults.get();

                        })
    }
}
