package HTTP_6;

import Util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;

public class SimpleHTTPServer extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.runExample(SimpleHTTPServer.class);
    }

    @Override
    public void start() {
        vertx.createHttpServer().requestHandler(httpServerRequest -> {
           httpServerRequest.response()
           .putHeader("content-type", "text/html")
           .end("<html><body><h1>Data and Persistence Team Rocks</h1></body></html>");
        }).listen(8080);
    }
}
