package HTTP_6;

import io.vertx.reactivex.core.AbstractVerticle;

public class SimpleHTTPServerV2 extends AbstractVerticle {

    @Override
    public void start() {
        vertx.createHttpServer().requestHandler(
                httpServerRequest -> httpServerRequest.response()
                        .putHeader("content-type", "text/html")
                        .end("<html><body><h1>Hello from " + this + "</h1></body></html>")
        ).listen(8080);

    }
}
