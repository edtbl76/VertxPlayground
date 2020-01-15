package HTTP_6;

import Util.Runner;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;

import java.nio.file.Paths;

public class HTTPSServer extends AbstractVerticle {
    public static void main(String[] args) {
        Runner.runExample(HTTPSServer.class);
    }

    @Override
    public void start() {
        HttpServer secureServer =
                vertx.createHttpServer(
                        new HttpServerOptions()
                        .setSsl(true)
                        .setKeyStoreOptions(
                                new JksOptions()
                                .setPath(Paths.get("").toAbsolutePath()
                                        + "/src/main/resources/"
                                        + "server-keystore.jks")
                                .setPassword("wibble")
                        ));

        secureServer.requestHandler(httpServerRequest -> {
            httpServerRequest.response()
                    .putHeader("content-type", "text/html")
                    .end("<html><body><h1>DNP Rocks Baby!</h1></body></html>");
        }).listen(4443);
    }
}
