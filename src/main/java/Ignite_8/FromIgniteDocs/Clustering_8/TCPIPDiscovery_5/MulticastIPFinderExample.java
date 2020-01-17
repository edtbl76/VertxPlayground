package Ignite_8.FromIgniteDocs.Clustering_8.TCPIPDiscovery_5;

import io.reactivex.functions.Consumer;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.spi.cluster.ignite.IgniteClusterManager;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;

public class MulticastIPFinderExample extends AbstractVerticle {

    static IgniteConfiguration igniteConfiguration = new IgniteConfiguration();

    public static void main(String[] args) {
        vertSetup();
    }

    @Override
    public void start() {
        TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();
        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
        ipFinder.setMulticastGroup("228.10.10.157");
        discoverySpi.setIpFinder(ipFinder);

        igniteConfiguration
                .setDiscoverySpi(discoverySpi)
                .setIgniteInstanceName("MulticastIPFinder");

        try (Ignite ignite = Ignition.start(igniteConfiguration)) {
            System.out.println(
                    "Name: " + ignite.configuration().getIgniteInstanceName() +
                    "\nDiscoverySpi: " + ignite.configuration().getDiscoverySpi().getName()
            );
        }
    }

    private static void vertSetup() {

        ClusterManager clusterManager = new IgniteClusterManager();
        EventBusOptions eventBusOptions = new EventBusOptions().setClustered(true);
        VertxOptions vertxOptions = new VertxOptions()
                .setEventBusOptions(eventBusOptions)
                .setClusterManager(clusterManager);

        Consumer<Vertx> runner = cv -> {
            try {
                cv.deployVerticle(MulticastIPFinderExample.class.getName());
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
