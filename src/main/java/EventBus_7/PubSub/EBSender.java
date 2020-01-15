package EventBus_7.PubSub;

import Util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.EventBus;

public class EBSender extends AbstractVerticle {
    public static void main(String[] args) {

        Runner.runClusteredExample(EBSender.class);
    }

    @Override
    public void start() {
        EventBus eb = vertx.eventBus();
        vertx.setPeriodic(1000, v -> eb.send("news", "DNP Rocks!"));
    }
}
