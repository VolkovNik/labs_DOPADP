package ru.bmstu.javascript.tester;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.concurrent.CompletionStage;

public class JavaScriptTester extends AllDirectives {
    public static void main(String[] args) throws ScriptException, NoSuchMethodException, IOException {

//        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
//        engine.eval("var divideFn = function(a,b) { return a/b}");
//        Invocable invocable = (Invocable) engine;
//        Object[] params = {2, 1};
//        System.out.println(invocable.invokeFunction("divideFn", params).toString());

        ActorSystem system = ActorSystem.create("routes");
        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        JavaScriptTester instance = new JavaScriptTester();
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow =
                instance.createRoute().flow(system, materializer);
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

    private Route createRoute() {
        return route(
                get(
                        () -> (parameter("PackageID", (id)) 
                ),
                post(
                        () -> complete("Recevied POST")
                )
        );
    }

}
