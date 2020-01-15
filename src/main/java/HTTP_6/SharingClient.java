package HTTP_6;

import Util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.client.WebClient;

public class SharingClient extends AbstractVerticle {
    public static void main(String[] args) {
        Runner.runExample(SharingClient.class);
    }

    @Override
    public void start() {
        WebClient client = WebClient.create(vertx);
        vertx.setPeriodic(1000, l -> client
                .get(8080, "localhost", "/")
                .send(httpResponseAsyncResult -> {
                   if (httpResponseAsyncResult.succeeded()) {
                       System.out.println("StatusCode: " + httpResponseAsyncResult.result().statusCode());
                       System.out.println("Got Data? -- " + httpResponseAsyncResult.result().bodyAsString());
                   } else {
                       System.out.println(httpResponseAsyncResult.cause().getMessage());
                   }
                }));
    }
}
