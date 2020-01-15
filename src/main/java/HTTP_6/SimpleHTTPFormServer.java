package HTTP_6;

import Util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;

import java.nio.file.Paths;

public class SimpleHTTPFormServer extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.runExample(SimpleHTTPFormServer.class);
    }

    @Override
    public void start() {
        vertx.createHttpServer().requestHandler(httpServerRequest -> {
           if (httpServerRequest.uri().equals("/")) {
               // Serve Index
               httpServerRequest.response().sendFile(Paths.get("").toAbsolutePath()
                       + "/src/main/java/HTTP_6/index.html");
           } else if (httpServerRequest.uri().startsWith("/form")) {
               httpServerRequest.response().setChunked(true);
               httpServerRequest.setExpectMultipart(true);
               httpServerRequest.endHandler(v -> {
                   httpServerRequest.formAttributes().names().forEach(attr ->
                           httpServerRequest.response().write("Got attr " + attr + " : "
                           + httpServerRequest.formAttributes().get(attr) + "\n"));
                   httpServerRequest.response().end();
               });
           } else {
               httpServerRequest.response().setStatusCode(404).end();
           }
        }).listen(8080);
    }
}
