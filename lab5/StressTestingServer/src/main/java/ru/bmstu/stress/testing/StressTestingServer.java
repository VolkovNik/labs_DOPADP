package ru.bmstu.stress.testing;

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
import akka.japi.Pair;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import scala.concurrent.Future;

import java.io.IOException;
import java.util.concurrent.CompletionStage;


public class StressTestingServer extends AllDirectives {

    private final static String GET_PARAMETER = "packageId";
    private final static String MSG_TEST_ACCEPT = "Test Accepted \n";
    private final static int TIMEOUT_FOR_FUTURE = 5;
    private final static String SYSTEM_NAME = "routes";
    private final static String HOST = "localhost";
    private final static int PORT = 8080;
    private final static String MSG_RUNNING_SERVER = "Server online at http://localhost:8080/\nPress RETURN to stop...";


    public static void main(String[] args) throws IOException {

        ActorSystem system = ActorSystem.create(SYSTEM_NAME);
        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        ActorRef actorCache = 
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = createFlow(materializer); // вызов метода которому передаем HTTP, ActorSystem и ActorMaterializer
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

    public static Flow<HttpRequest, HttpResponse, NotUsed> createFlow(ActorMaterializer materializer) {
        return Flow.of(HttpRequest.class)
                .map(
                        (req) -> {
                            return new Pair<>(1, 1);
                        }
                ).map(
                        (Pair<Integer, Integer> p) -> {
                            // TODO послать в кэширующий актор
                            return HttpResponse.create().withEntity("hello response" + p);
                        }
                );
    }

}