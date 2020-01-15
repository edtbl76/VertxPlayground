package HTTP_6;

import Util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;

public class SimpleHTTPClient extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.runExample(SimpleHTTPClient.class);
    }

    @Override
    public void start() {
        WebClient client = WebClient.create(vertx);
        client
                .get(8080, "localhost", "/")
                .send(httpResponseAsyncResult -> {
                    if (httpResponseAsyncResult.succeeded()) {
                        HttpResponse<Buffer> response = httpResponseAsyncResult.result();
                        System.out.println("Received response with status code: " + response.statusCode());
                        System.out.println("Got Data?: " + response.bodyAsString());
                    } else {
                        System.out.println("Shit happens: " + httpResponseAsyncResult.cause().getMessage());
                    }
                });
    }
}
