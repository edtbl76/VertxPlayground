package EmbeddedServer_1;

import io.vertx.reactivex.core.Vertx;

public class EmbeddedServer {

    public static void main(String[] args) {
        Vertx.vertx().createHttpServer().requestHandler(
                        httpServerRequest -> httpServerRequest.response().end("Hello World!")
        ).listen(8080);
    }
}
