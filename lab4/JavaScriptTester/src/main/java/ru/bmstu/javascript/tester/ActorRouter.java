package ru.bmstu.javascript.tester;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.RoundRobinPool;

public class ActorRouter extends AbstractActor {

    private final ActorRef actorExecutor = getContext().actorOf(
            new RoundRobinPool(5).props(Props.create(ActorExecutor.class)));
    private final ActorRef actorStorage = getContext().actorOf(Props.create(ActorStorage.class));


    @Override
    public Receive createReceive() {
        return null;
    }
}
