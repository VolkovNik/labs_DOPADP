package ru.bmstu.javascript.tester;

import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.stream.ActorMaterializer;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JavaScriptTester {
    public static void main(String[] args) throws ScriptException, NoSuchMethodException {

//        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
//        engine.eval("var divideFn = function(a,b) { return a/b}");
//        Invocable invocable = (Invocable) engine;
//        Object[] params = {2, 1};
//        System.out.println(invocable.invokeFunction("divideFn", params).toString());

        ActorSystem system = ActorSystem.create("routes");
        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        MainHttp instance = new MainHttp(system);
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow =
                instance.createRoute(system).flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                routeFlow,
                ConnectHttp.toHost("localhost", 8080),
                materializer
                System.out.println("Server online at http://localhost:8080/\nPress RETURN to stop...");
        System.in.read();
        binding
                .thenCompose(ServerBinding::unbind)
                .thenAccept(unbound -> system.terminate());

    }
}
