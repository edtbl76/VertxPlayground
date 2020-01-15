package EventBus_7.P2P;

import Util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.EventBus;

public class P2PConsumer extends AbstractVerticle {
    public static void main(String[] args) {
        Runner.runClusteredExample(P2PConsumer.class);
    }

    @Override
    public void start() {
        EventBus eventBus = vertx.eventBus();

        eventBus.consumer("my-address", message -> {
           System.out.println("Message RCVD: " + message.body());
           message.reply("pong!");
        });

    }
}
