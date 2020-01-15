package Ignite_8.FromIgniteDocs.ThreadPool_3;

import io.reactivex.functions.Consumer;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.spi.cluster.ignite.IgniteClusterManager;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteLogger;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.ExecutorConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.logger.slf4j.Slf4jLogger;

public class IgniteCustomThreadPool extends AbstractVerticle {

    public static void main(String[] args) {
        /*
            Set up custom thread pool.
         */
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        IgniteLogger igniteLogger = new Slf4jLogger();
        igniteConfiguration
                .setExecutorConfiguration(new ExecutorConfiguration("custom-pool").setSize(8))
                .setGridLogger(igniteLogger);

        ClusterManager clusterManager = new IgniteClusterManager(igniteConfiguration);
        EventBusOptions eventBusOptions = new EventBusOptions().setClustered(true);
        VertxOptions vertxOptions = new VertxOptions()
                .setEventBusOptions(eventBusOptions)
                .setClusterManager(clusterManager);

        Consumer<Vertx> runner = consumerVertx -> {
            try {
                consumerVertx.deployVerticle(IgniteCustomThreadPool.class.getName());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        };

        Vertx.clusteredVertx(vertxOptions, vertxAsyncResult -> {
            if (vertxAsyncResult.succeeded()) {
                Vertx vertx = vertxAsyncResult.result();
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
            ignite.compute().run(new OuterRunnable());
        }
    }
}
