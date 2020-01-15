package EventBus_7.P2P;

import Util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.EventBus;

public class P2PProducer extends AbstractVerticle {
    public static void main(String[] args) {
        Runner.runClusteredExample(P2PProducer.class);
    }

    @Override
    public void start() {
        EventBus eventBus = vertx.eventBus();

        vertx.setPeriodic(1000, v -> {
            eventBus.request("my-address", "ping", messageAsyncResult -> {
                if (messageAsyncResult.succeeded())
                    System.out.println("Response RCVD: " + messageAsyncResult.result().body());
                else
                    System.out.println("Response not received");

            });
        });

    }
}
