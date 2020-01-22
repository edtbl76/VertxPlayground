package Ignite_8.FromIgniteDocs.Clustering_8.ZeroDeployment_7;

import io.reactivex.functions.Consumer;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.spi.cluster.ignite.IgniteClusterManager;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

public class Peer1 extends AbstractVerticle {

    static IgniteConfiguration igniteConfiguration = new IgniteConfiguration();

    public static void main(String[] args) {
        VertxOptions vertxOptions = new VertxOptions()
                .setEventBusOptions(new EventBusOptions().setClustered(true))
                .setClusterManager(new IgniteClusterManager());

        Consumer<Vertx> runner = cv -> cv.deployVerticle(Peer1.class.getName());

        Vertx.clusteredVertx(vertxOptions, event -> {
            if (event.succeeded()) {
                try {
                    runner.accept(event.result());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                event.cause().printStackTrace();
            }
        });
    }


    @Override
    public void start() {

        try (Ignite ignite = Ignition.start()) {
            System.out.println("Hello World!");
        }
    }
}

