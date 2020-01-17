package Ignite_8.FromIgniteDocs.Clustering_8.ClusterAPI;

import io.reactivex.functions.Consumer;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.spi.cluster.ignite.IgniteClusterManager;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCluster;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.cluster.ClusterMetrics;
import org.apache.ignite.cluster.ClusterNode;

import java.util.Collection;

public class IgniteClusterAPIEx extends AbstractVerticle {

    public static void main(String[] args) {
        vertSetup();
    }

    @Override
    public void start() {
        try (Ignite ignite = Ignition.start()) {

            // Instantiate cluster interface.
            IgniteCluster igniteCluster = ignite.cluster();

            ClusterGroup workers = igniteCluster.forServers();
            Collection<ClusterNode> nodes = workers.nodes();

            // Dump All of the values:
            nodes.forEach(
                    clusterNode -> {
                        System.out.println("Node: " + clusterNode.id());
                        clusterNode.attributes().forEach((key, value) -> System.out.println("\t" + key + ": " + value));
                    }
            );

            // Get some metrics
            ClusterNode localNode = igniteCluster.localNode();

            ClusterMetrics clusterMetrics = localNode.metrics();
            System.out.println(
                    "Current CPU Load       : " + clusterMetrics.getCurrentCpuLoad() +
                    "\nAverage Rejected Jobs  : " + clusterMetrics.getAverageRejectedJobs() +
                    "\nBusy Time Percentage   : " + clusterMetrics.getBusyTimePercentage() +
                    "\nUpTime                 : " + clusterMetrics.getUpTime() +
                    "\nTotalNodes             : " + clusterMetrics.getTotalNodes() +
                            "\nAnd a boat load more!"
            );
        }
    }

    private static void vertSetup() {
        ClusterManager clusterManager = new IgniteClusterManager();
        EventBusOptions eventBusOptions = new EventBusOptions().setClustered(true);
        VertxOptions vertxOptions = new VertxOptions()
                .setEventBusOptions(eventBusOptions)
                .setClusterManager(clusterManager);

        Consumer<Vertx> runner =  consumerVertx -> {
            try {
                consumerVertx.deployVerticle(IgniteClusterAPIEx.class.getName());
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
