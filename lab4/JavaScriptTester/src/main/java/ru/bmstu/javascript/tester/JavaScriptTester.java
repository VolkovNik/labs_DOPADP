package ru.bmstu.javascript.tester;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;
import akka.routing.RoundRobinPool;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import scala.concurrent.Future;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.concurrent.CompletionStage;

public class JavaScriptTester extends AllDirectives {
    public static void main(String[] args) throws ScriptException, NoSuchMethodException, IOException {

        ActorSystem system = ActorSystem.create("routes");
        final Http http = Http.get(system);
        ActorRef router  = system.actorOf(Props.create(ActorRouter.class));
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        JavaScriptTester instance = new JavaScriptTester();
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow =
                instance.createRoute(router).flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                routeFlow,
                ConnectHttp.toHost("localhost", 8080),
                materializer);

        System.out.println("Server online at http://localhost:8080/\nPress RETURN to stop...");
        System.in.read();
        binding
                .thenCompose(ServerBinding::unbind)
                .thenAccept(unbound -> system.terminate());

    }

    private Route createRoute(ActorRef router) {
        return route(
                get(
                        () -> parameter("packageId",
                                (id) -> {
                                    Future<Object> future = Patterns.ask(router, new GetResultMsg(id), 5);
                                    return completeOKWithFuture(future, Jackson.marshaller());
                                })
                ),
                post(
                        () -> entity(Jackson.unmarshaller(RequestBody.class),
                                (requestBody) -> complete(requestBody.getFunctionName() + "\n"))
                )
        );
    }

}
