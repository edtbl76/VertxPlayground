package Net_5;

import Util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.net.NetSocket;
import io.vertx.reactivex.core.parsetools.RecordParser;

import java.util.stream.Stream;

public class RPClient extends AbstractVerticle {
    public static void main(String[] args) {
        Runner.runExample(RPClient.class);
    }

    @Override
    public void start() {
        vertx.createNetClient().connect(1234, "localhost",
                netSocketAsyncResult -> {
                    if (netSocketAsyncResult.succeeded()) {
                        NetSocket socket = netSocketAsyncResult.result();

                        RecordParser.newDelimited("\n", socket)
                                .endHandler(v-> socket.close())
                                .exceptionHandler(throwable -> {
                                    throwable.printStackTrace();
                                    socket.close();
                                })
                                .handler(buffer -> System.out.println("RP Client RECV: " + buffer));

                        Stream.of("Data", "Persistence", "Rocks").forEach(name -> {
                            System.out.println("RP Client SEND: " + name);
//                            socket.write(name).write("\n");
                        });
                    } else {
                        System.out.println("Failed to connect " + netSocketAsyncResult.cause());
                    }
                });
    }
}
