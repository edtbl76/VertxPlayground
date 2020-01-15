package Ignite_8.FromIgniteDocs.ClientServer_2;

import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.spi.cluster.ignite.IgniteClusterManager;
import org.apache.ignite.*;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;

import javax.cache.CacheException;
import java.util.function.Consumer;

public class IgniteClientExample extends AbstractVerticle {
    public static void main(String[] args) {
//        Runner.runClusteredExample(IgniteClientExample.class);


        /*
            Start ignite config stuff
         */
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();

        // Specify subsetting hierarchies
        TcpCommunicationSpi communicationSpi = new TcpCommunicationSpi();
        TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();

        // Slow Client Setting
        communicationSpi.setSlowClientQueueLimit(1000);

        // disable auto client reconnect
        discoverySpi.setClientReconnectDisabled(true);;

        // Wire sub settings to igniteConfig.
        igniteConfiguration
                .setCommunicationSpi(communicationSpi)
                .setDiscoverySpi(discoverySpi);

        // Establish Cluster Manager
        ClusterManager clusterManager = new IgniteClusterManager(igniteConfiguration);
        EventBusOptions eventBusOptions = new EventBusOptions().setClustered(true);
        VertxOptions vertxOptions = new VertxOptions()
                .setEventBusOptions(eventBusOptions).setClusterManager(clusterManager);


        Consumer<Vertx> runner = vertx1 -> {
            try {
                vertx1.deployVerticle(IgniteClientExample.class.getName());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        };

        Vertx.clusteredVertx(vertxOptions, vertxAsyncResult -> {
            if (vertxAsyncResult.succeeded()) {
                Vertx vertx = vertxAsyncResult.result();
                runner.accept(vertx);
            } else {
                vertxAsyncResult.cause().printStackTrace();
            }
        });
    }

    @Override
    public void start() {

        CacheConfiguration<String, String> config = new CacheConfiguration<>("my-cache");

        Ignition.setClientMode(true);
        igniteClientServer(config);
    }

    private static void igniteClientServer(CacheConfiguration<String, String> config) {

        try(Ignite ignite = Ignition.start()) {
            IgniteCache<String, String> igniteCache = ignite.getOrCreateCache(config);

            /*
                This code isn't necessary, because clientMode is identified in the default logging.
                However, I'm leaving it here to demonstrate that we can differentiate programmatically.
             */
//            if (Ignition.isClientMode()) {
//                System.out.println("\n\n\nI'm a client\n\n\n");
//            } else {
//                System.out.println("\n\n\nI'm a server\n\n\n");
//            }


            /*
                I've added some client handling just to demo the depth of the client support.
             */


            // Identify My Cache.
            try {
                System.out.println("[" + igniteCache.getName() + "] " + "Empty Cache -> ");
                igniteCache.forEach(System.out::println);
                System.out.println("<- Empty Cache [" + igniteCache.getName() + "]");
            } catch (CacheException e) {
                if (e.getCause() instanceof IgniteClientDisconnectedException) {
                    IgniteClientDisconnectedException cause = (IgniteClientDisconnectedException)e.getCause();
                    cause.reconnectFuture().get();
                }
            }

            ClusterGroup cultureClub = ignite.cluster().forClients();
            IgniteCompute clientCompute = ignite.compute(cultureClub);


            try {
                clientCompute.broadcast(() -> System.out.println("Do you really want to hurt me?"));
            } catch (IgniteClientDisconnectedException icde) {
                icde.reconnectFuture().get();
                /*
                    We can put code here to continue processing without having to construct a new
                    instance of compute.
                 */
            }
        }
    }

}
