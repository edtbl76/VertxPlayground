package Net_5;

import Util.Runner;
import io.vertx.core.net.NetClientOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.net.NetSocket;

import java.nio.charset.Charset;
import java.util.stream.IntStream;

public class EchoClientSSL extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.runExample(EchoClientSSL.class);
    }

    @Override
    public void start() {

        NetClientOptions sslOptions  = new NetClientOptions()
                .setSsl(true)
                .setTrustAll(true);         // Prototype Only. Please don't do this.

        vertx.createNetClient(sslOptions).connect(1234, "localhost",
                netSocketAsyncResult -> {
                    if (netSocketAsyncResult.succeeded()) {
                        NetSocket socket = netSocketAsyncResult.result();
                        socket.handler(buffer ->
                                System.out.println("Client Recv:: " + buffer.toString(Charset.defaultCharset())));

                        IntStream.rangeClosed(1, 10).forEach(value -> {
                            String str = "hello " + value + "\n";
                            System.out.println("TCP Client SEND: " + str);
                            socket.write(str);
                        });
                    } else{
                        System.out.println("Failed to Connect: " + netSocketAsyncResult.cause());
                    }
                });
    }
}
