package Ignite_8.FromIgniteDocs.AsynchronousSupport_4;

import io.reactivex.functions.Consumer;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.spi.cluster.ignite.IgniteClusterManager;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lang.IgniteFuture;

public class AsyncExample extends AbstractVerticle {

    public static void main(String[] args) {
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();

        ClusterManager clusterManager = new IgniteClusterManager();
        EventBusOptions eventBusOptions = new EventBusOptions().setClustered(true);
        VertxOptions vertxOptions = new VertxOptions()
                .setEventBusOptions(eventBusOptions)
                .setClusterManager(clusterManager);

        Consumer<Vertx> runner = consumerVertx -> {
            try {
                consumerVertx.deployVerticle(AsyncExample.class.getName());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        };

        Vertx.clusteredVertx(vertxOptions, vertxAsyncResult -> {
            if (vertxAsyncResult.succeeded()) {
                Vertx vertx = Vertx.vertx();
                try {
                    runner.accept(vertx);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                vertxAsyncResult.cause().printStackTrace();
            }
        });
    }

    @Override
    public void start() {

        try(Ignite ignite = Ignition.start()) {
            IgniteCompute compute = ignite.compute();

            IgniteFuture<String> igniteFuture = compute.callAsync(() -> "I'm a sink.");

            igniteFuture.listen(stringIgniteFuture -> System.out.println("Job Result: " + stringIgniteFuture.get()));
        }
    }
}
