package ExecuteBlockingForDBs_4;

import Util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;

public class ExecuteBlockingExample extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.runExample(ExecuteBlockingExample.class);
    }

    @Override
    public void start() throws Exception {
        vertx.createHttpServer().requestHandler(httpServerRequest -> {

            vertx.<String>executeBlocking(
                    stringPromise -> {
                        // Here is our blocking code!

                        try {
                            Thread.sleep(500);
                        } catch (Exception ignore) {}

                        stringPromise.complete("Data and Persistence Rocks!");
                    },
                    stringAsyncResult -> {
                        if (stringAsyncResult.succeeded()) {
                            httpServerRequest.response()
                                    .putHeader("content-type", "text/plain")
                                    .end(stringAsyncResult.result());
                        } else {
                            stringAsyncResult.cause().printStackTrace();
                        }
                    }
            );
        }).listen(8080);
    }
}
