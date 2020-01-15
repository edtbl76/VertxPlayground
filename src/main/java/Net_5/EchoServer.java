package Net_5;

import Util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.streams.Pump;

public class EchoServer extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.runExample(EchoServer.class);
    }

    @Override
    public void start() throws Exception {
        vertx.createNetServer().connectHandler(netSocket -> {
            Pump.pump(netSocket, netSocket).start();
        }).listen(1234);

        System.out.println("Echo server is listening");
    }
}
