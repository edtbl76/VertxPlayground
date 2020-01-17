package Ignite_8.FromIgniteDocs.Clustering_8.ClusterGroups_3;

import io.reactivex.functions.Consumer;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.spi.cluster.ignite.IgniteClusterManager;
import org.apache.ignite.*;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.cluster.ClusterMetrics;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

import java.util.Collections;
import java.util.Map;

public class ClusterGroupExample2 extends AbstractVerticle {

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
                System.out.println("I'm Thing 2");

                /*
                    Demonstrating "Getting Nodes" and "Combining Cluster Groups"
                 */
                System.out.println("Remote Nodes: ");
                ClusterGroup remotes = igniteCluster.forRemotes();
                remotes.nodes().forEach(clusterNode -> System.out.println("Node: " + clusterNode.id()));

                ClusterNode oldest = remotes.forOldest().node();
                ClusterNode youngest = remotes.forYoungest().node();
                System.out.println("Oldest   : " + oldest.id());
                System.out.println("Youngest : " + youngest.id());


                /*
                    Demonstrate getting the specific node from a group if you have the id.
                 */
                System.out.println("Oldest From Remotes (By Id)  : " + remotes.node(oldest.id()).id());
                System.out.println("Youngest From Remotes (By Id): " + remotes.node(youngest.id()).id());


                /*
                    Metrics
                 */
                ClusterMetrics metrics = remotes.metrics();
                System.out.println("Remote Nodes        : " + metrics.getTotalNodes());
                System.out.println("Remote UpTime       : " + metrics.getUpTime());
                System.out.println("Remote ActiveJobs   : " + metrics.getCurrentActiveJobs());
                System.out.println("Remote NodeStartTime: " + metrics.getNodeStartTime());
                System.out.println("And so very many more!");
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
                consumerVertx.deployVerticle(ClusterGroupExample2.class.getName());
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
