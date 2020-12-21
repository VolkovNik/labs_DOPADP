package ru.bmstu.stress.testing;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.Query;
import akka.http.javadsl.server.AllDirectives;
import akka.japi.Pair;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.*;

import org.asynchttpclient.*;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;

import static org.asynchttpclient.Dsl.asyncHttpClient;


public class StressTestingServer extends AllDirectives {

    private final static Duration TIMEOUT = Duration.ofMillis(5);
    private final static String SYSTEM_NAME = "routes";
    private final static String HOST = "localhost";
    private final static int PORT = 8080;
    private final static String MSG_RUNNING_SERVER = "Server online at http://localhost:8080/\nPress RETURN to stop...";
    private final static String AVRG_TIME_CONNECTING_STR = "average time connecting to ";
    private final static String IS_STR = " is ";
    private final static String MILLIS_STR = "millis";
    private final static String NEW_LINE_SYMBOL = "\n";
    private final static String FIRST_QUERY_PARAM = "testUrl";
    private final static String SECOND_QUERY_PARAM = "count";


    public static void main(String[] args) throws IOException {

        ActorSystem system = ActorSystem.create(SYSTEM_NAME);
        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        ActorRef actorCache = system.actorOf(Props.create(ActorCache.class));
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = createFlow(materializer, actorCache); // вызов метода которому передаем HTTP, ActorSystem и ActorMaterializer
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

    public static Flow<HttpRequest, HttpResponse, NotUsed> createFlow(ActorMaterializer materializer, ActorRef actorCache) {
        return Flow.of(HttpRequest.class)
                .map(
                        (req) -> {
                            Query queryParams = req.getUri().query();
                            String URL = queryParams.get(FIRST_QUERY_PARAM).get();
                            Integer count = Integer.parseInt(queryParams.get(SECOND_QUERY_PARAM).get());
                            return new Pair<>(URL, count);
                        }).mapAsync(
                        1, (Pair<String, Integer> p) -> {
                            FindResultMsg findResultMsg = new FindResultMsg(p.first());
                            CompletionStage<Object> answer = Patterns.ask(actorCache, findResultMsg, TIMEOUT);
                            return answer.thenCompose(
                                    (Object ans) -> {
                                        if ((Integer)ans >= 0) {
                                            return CompletableFuture.completedFuture(new Pair<>(p.first(), (Integer)ans));
                                        }
                                        Flow<Pair<String, Integer>, Integer, NotUsed> flow =
                                                Flow.<Pair<String, Integer>>create()
                                                .mapConcat(pair -> {
                                                    List<String> responses = Collections.nCopies(pair.second(), pair.first());
                                                    return responses;
                                                })
                                                .mapAsync(
                                                        p.second(), (String testUrl) -> {
                                                            AsyncHttpClient asyncHttpClient = asyncHttpClient();
                                                            Instant timeStart = Instant.now();
                                                            Future<Response> whenResponse = asyncHttpClient.prepareGet(testUrl).execute();
                                                            whenResponse.get();
                                                            long timeFull = timeStart.until(Instant.now(), ChronoUnit.MILLIS);
                                                            return CompletableFuture.completedFuture((int) timeFull);
                                                        }
                                                );
                                        Source<Pair<String, Integer>, NotUsed> source = Source.single(p);
                                        Sink<Integer, CompletionStage<Integer>> fold = Sink.fold(0, Integer::sum);
                                        RunnableGraph<CompletionStage<Integer>> runnableGraph = source.via(flow).toMat(fold, Keep.right());
                                        CompletionStage<Integer> result = runnableGraph.run(materializer);
                                        return result.thenApply(
                                                res -> new Pair<>(p.first(), res/p.second())
                                        );
                                    }
                            );
                        }).map(
                        (Pair<String, Integer> p) -> {
                            StoreResultMsg storeResultMsg = new StoreResultMsg(p.first(), p.second());
                            actorCache.tell(storeResultMsg, ActorRef.noSender());
                            return HttpResponse.create().withEntity(AVRG_TIME_CONNECTING_STR + p.first()
                                    + IS_STR + p.second() + MILLIS_STR + NEW_LINE_SYMBOL);
                        }
                );
    }

}
