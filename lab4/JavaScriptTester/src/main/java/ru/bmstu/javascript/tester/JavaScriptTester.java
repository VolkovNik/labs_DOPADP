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
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import ru.bmstu.javascript.tester.actors.ActorRouter;
import ru.bmstu.javascript.tester.utils.RequestBody;
import scala.concurrent.Future;

import java.io.IOException;
import java.util.concurrent.CompletionStage;

import ru.bmstu.javascript.tester.messages.*;

public class JavaScriptTester extends AllDirectives {

    private final static String GET_PARAMETER = "packageId";
    private final static String MSG_TEST_ACCEPT = "Test Accepted \n";
    private final static int TIMEOUT_FOR_FUTURE = 5;
    private final static String SYSTEM_NAME = "routes";
    private final static String HOST = "localhost";
    private final static int PORT = 8080;
    private final static String MSG_RUNNING_SERVER = "Server online at http://localhost:8080/\nPress RETURN to stop...";

    private Route createRoute(ActorRef router) {
        return route(
                get(
                        () -> parameter(GET_PARAMETER,
                                (id) -> {
                                    Future<Object> future = Patterns.ask(router, new GetResultMsg(id), TIMEOUT_FOR_FUTURE);
                                    return completeOKWithFuture(future, Jackson.marshaller());
                                })
                ),
                post(
                        () -> entity(Jackson.unmarshaller(RequestBody.class),
                                (requestBody) -> {
                                    router.tell(requestBody, ActorRef.noSender());
                                    return complete(MSG_TEST_ACCEPT);
                                })
                )
        );
    }

    public static void main(String[] args) throws IOException {

        ActorSystem system = ActorSystem.create(SYSTEM_NAME);
        final Http http = Http.get(system);

        final ActorMaterializer materializer = ActorMaterializer.create(system);
        JavaScriptTester instance = new JavaScriptTester();
        ActorRef router  = system.actorOf(Props.create(ActorRouter.class));
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow =
                instance.createRoute(router).flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                routeFlow,
                ConnectHttp.toHost(HOST, PORT),
                materializer);

        System.out.println(MSG_RUNNING_SERVER);
        System.in.read();
        binding
                .thenCompose(ServerBinding::unbind)
                .thenAccept(unbound -> system.terminate());

    }

}
