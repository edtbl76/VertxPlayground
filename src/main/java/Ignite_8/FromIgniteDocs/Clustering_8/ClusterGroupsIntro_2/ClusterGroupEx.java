package Ignite_8.FromIgniteDocs.Clustering_8.ClusterGroupsIntro_2;

import io.reactivex.functions.Consumer;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.reactivex.core.*;
import io.vertx.spi.cluster.ignite.IgniteClusterManager;
import org.apache.ignite.*;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

import java.util.Collections;
import java.util.Map;

public class ClusterGroupEx extends AbstractVerticle {

    static IgniteConfiguration igniteConfiguration = new IgniteConfiguration();

    public static void main(String[] args) {
        vertSetup();
    }

    @Override
    public void start() {
        try (Ignite ignite = Ignition.start(igniteConfiguration)) {
            IgniteCluster igniteCluster = ignite.cluster();

            CacheConfiguration<String, String> cacheConfiguration = new CacheConfiguration<>();
            cacheConfiguration.setCacheMode(CacheMode.PARTITIONED);
            cacheConfiguration.setName("just-a-cache");
            cacheConfiguration.setAtomicityMode(CacheAtomicityMode.ATOMIC);

            try (IgniteCache<String, String> igniteCache = ignite.getOrCreateCache(cacheConfiguration)) {

               // Local Nodes
                IgniteCompute compute = ignite.compute(igniteCluster);
                compute.broadcast(() -> System.out.println("Local Node: " + igniteCluster.localNode().id()));

                // Remote Nodes
                compute = ignite.compute(igniteCluster.forRemotes());
                compute.broadcast(() -> System.out.println("Remote Node: " + igniteCluster.localNode().id()));

                // Cache Nodes
                compute = ignite.compute(igniteCluster.forCacheNodes("just-a-cache"));
                compute.broadcast(() -> System.out.println("Cache Node: " + igniteCluster.localNode().id()));

                // Data Nodes ... same as Cache Nodes??
                compute = ignite.compute(igniteCluster.forDataNodes("just-a-cache"));
                compute.broadcast(() -> System.out.println("Data Node: " + igniteCluster.localNode().id()));

                // Nodes w/ attributes
                compute = ignite.compute(igniteCluster.forAttribute("userAttribute", "attributeName"));
                compute.broadcast(() -> System.out.println("Attr Node: " + igniteCluster.localNode().id()));

                // Get a randomm node
                ClusterGroup random = igniteCluster.forRandom();
                compute = ignite.compute(random);
                compute.broadcast(() -> System.out.println("Random Node: " + igniteCluster.localNode().id()));

                // Get everything ono the same host as a randomly selected nodoe
                compute = ignite.compute(igniteCluster.forHost((ClusterNode) random));
                compute.broadcast(() -> System.out.println("My Host Node: " + igniteCluster.localNode().id()));

                // Oldest node
                compute = ignite.compute(igniteCluster.forOldest());
                compute.broadcast(() -> System.out.println("Oldest Node: " + igniteCluster.localNode().id()));

                // Custom  node
                compute = ignite.compute(igniteCluster.forPredicate((node) ->  node.metrics().getCurrentCpuLoad() > 0.0));
                compute.broadcast(() -> System.out.println("CustomNode: " + igniteCluster.localNode().id()));


            } catch (IgniteException ignore) { }
            finally {
                ignite.destroyCache("just-a-cache");
            }
        }
    }

    private static void vertSetup() {
        Map<String, String> maps = Collections.singletonMap("userAttribute", "attributeName");
        igniteConfiguration.setUserAttributes(maps);

        ClusterManager clusterManager = new IgniteClusterManager();
        EventBusOptions eventBusOptions = new EventBusOptions().setClustered(true);
        VertxOptions vertxOptions = new VertxOptions()
                .setEventBusOptions(eventBusOptions)
                .setClusterManager(clusterManager);


        Consumer<Vertx> runner = consumerVertx -> {
            try {
                consumerVertx.deployVerticle(ClusterGroupEx.class.getName());
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
