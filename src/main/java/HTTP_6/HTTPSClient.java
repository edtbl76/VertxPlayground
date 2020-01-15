package HTTP_6;

import Util.Runner;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.client.WebClient;

public class HTTPSClient extends AbstractVerticle {
    public static void main(String[] args) {
        Runner.runExample(HTTPSClient.class);
    }

    @Override
    public void start() {
        WebClientOptions opts = new WebClientOptions()
                .setSsl(true)
                .setTrustAll(true);     // Please don't do this in production.

        WebClient client = WebClient.create(vertx, opts);
        client.get(4443, "localhost", "/")
                .ssl(true)
                .send(httpResponseAsyncResult -> {
                    if (httpResponseAsyncResult.succeeded()) {
                        System.out.println("Status Code:  " + httpResponseAsyncResult.result().statusCode());
                        System.out.println("Got Data   :  " + httpResponseAsyncResult.result().bodyAsString());
                    } else {
                        httpResponseAsyncResult.cause().printStackTrace();
                        System.out.println("Shit Done Broke: " + httpResponseAsyncResult.cause().getMessage());
                    }
                });

    }
}
