package EventBus_7.PubSub;

import Util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.EventBus;

public class EBReceiver extends AbstractVerticle {
    public static void main(String[] args) {

        Runner.runClusteredExample(EBReceiver.class);
    }

    @Override
    public void start() {
        EventBus eb = vertx.eventBus();

        eb.consumer("news", message -> System.out.println("Received on Consumer 1: " + message.body()));
        eb.consumer("news", message -> System.out.println("Received on Consumer 2: " + message.body()));
        eb.consumer("news", message -> System.out.println("Received on Consumer 3: " + message.body()));
        System.out.println("Ready!");

    }
}
