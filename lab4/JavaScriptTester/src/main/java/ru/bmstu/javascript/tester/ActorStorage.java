package ru.bmstu.javascript.tester;

import akka.actor.AbstractActor;

import java.util.ArrayList;
import java.util.HashMap;

public class ActorStorage extends AbstractActor {
    private final HashMap<String, ArrayList<String>> testResults = new HashMap<>();

    

}
