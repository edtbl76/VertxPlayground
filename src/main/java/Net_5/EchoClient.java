package Net_5;

import Util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.net.NetSocket;

import java.util.stream.IntStream;

public class EchoClient extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.runExample(EchoClient.class);
    }

    @Override
    public void start() {

        vertx.createNetClient().connect(1234, "localhost", netSocketAsyncResult -> {

            if (netSocketAsyncResult.succeeded()) {
                NetSocket socket = netSocketAsyncResult.result();
                socket.handler(
                        buffer ->
                                System.out.println("TCP Client receiving:  " + buffer.toString("UTF-8")));

                IntStream.rangeClosed(0, 9).forEach(value -> {
                    String str = "hello " + value + "\n";
                    System.out.println("Net client sending: " + str);
                    socket.write(str);
                });
            } else {
                System.out.println("Failed to connect: " + netSocketAsyncResult.cause());
            }
        });

    }
}
