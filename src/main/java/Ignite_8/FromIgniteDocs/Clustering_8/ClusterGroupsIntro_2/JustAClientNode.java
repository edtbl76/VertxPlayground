package Ignite_8.FromIgniteDocs.Clustering_8.ClusterGroupsIntro_2;

import io.reactivex.functions.Consumer;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.spi.cluster.ignite.IgniteClusterManager;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

public class JustAClientNode extends AbstractVerticle {

    static IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
    public static void main(String[] args) {
        vertSetup();
    }

    @Override
    public void start() {
        igniteConfiguration.setClientMode(true);

        try (Ignite ignite = Ignition.start(igniteConfiguration)) {

            CacheConfiguration<String, String> cacheConfiguration = new CacheConfiguration<>();
            cacheConfiguration.setCacheMode(CacheMode.PARTITIONED);
            cacheConfiguration.setName("just-a-cache");
            cacheConfiguration.setAtomicityMode(CacheAtomicityMode.ATOMIC);

            // All Nodes
            try (IgniteCache<String, String> igniteCache = ignite.getOrCreateCache(cacheConfiguration)) {
                System.out.println("I'm just a client node");
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void vertSetup() {
        ClusterManager clusterManager = new IgniteClusterManager();
        EventBusOptions eventBusOptions = new EventBusOptions().setClustered(true);
        VertxOptions vertxOptions = new VertxOptions()
                .setEventBusOptions(eventBusOptions)
                .setClusterManager(clusterManager);


        Consumer<Vertx> runner = consumerVertx -> {
            try {
                consumerVertx.deployVerticle(JustAClientNode.class.getName());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        };

        Vertx.clusteredVertx(vertxOptions, vertxAsyncResult -> {
            if (vertxAsyncResult.succeeded()) {
                try {
                    runner.accept(vertxAsyncResult.result());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                vertxAsyncResult.cause().printStackTrace();
            }
        });
    }
}
