package Util;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.reactivex.core.Vertx;
import io.vertx.spi.cluster.ignite.IgniteClusterManager;
import org.apache.ignite.IgniteLogger;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.logger.slf4j.Slf4jLogger;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class Runner {

    private static final String BASE_JAVA = "/src/main/java/";

    public static void runClusteredExample(Class clazz) {
        /*
            On OS X, enable Multicast in order to get Multicast
            discovery to work.
                sudo route add -net 224.0.0.0/5 127.0.0.1

         */
        ClusterManager clusterManager = new IgniteClusterManager();

        // Build EventBus
        EventBusOptions eventBusOptions = new EventBusOptions().setClustered(true);

        // Build VertX Options
        VertxOptions vertxOptions = new VertxOptions()
                .setEventBusOptions(eventBusOptions).setClusterManager(clusterManager);

        runExample(BASE_JAVA, clazz, vertxOptions, null);
    }

    public static void runClusteredExample(
            Class clazz, TcpDiscoverySpi tcpDiscoverySpi, TcpCommunicationSpi tcpCommunicationSpi) {

        /*
            This is for NON multicast discovery.
            Everything here is split out deliberately to demonstrate the wiring required.
         */
        // Build Ignite Configuration
//        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
//        IgniteLogger igniteLogger = new Slf4jLogger();
//        igniteConfiguration
//                .setGridLogger(igniteLogger)
//                .setDiscoverySpi(tcpDiscoverySpi)
//                .setCommunicationSpi(tcpCommunicationSpi);

        // Build Cluster Manager
//        ClusterManager clusterManager = new IgniteClusterManager(igniteConfiguration);
        ClusterManager clusterManager = new IgniteClusterManager();

        // Build EventBus
        EventBusOptions eventBusOptions = new EventBusOptions().setClustered(true);

        // Build VertX Options
        VertxOptions vertxOptions = new VertxOptions()
                .setEventBusOptions(eventBusOptions).setClusterManager(clusterManager);

        runExample(BASE_JAVA, clazz, vertxOptions, null);
    }

    public static void runExample(Class clazz) {
        runExample(BASE_JAVA, clazz, new VertxOptions(), null);
    }

    public static void runExample(Class clazz, DeploymentOptions deployment) {
        runExample(BASE_JAVA, clazz, new VertxOptions(), deployment);
    }

    public static void runExample(String directory, Class clazz, VertxOptions options, DeploymentOptions deployment) {
        runExample(
                directory + clazz.getPackage().getName().replace(".", "/"),
                clazz.getName(),
                options, deployment);
    }

    public static void runExample(String directory, String id, VertxOptions options, DeploymentOptions deployment) {
        if (options == null) {
            options = new VertxOptions();
        }

        try {
            File current = new File(".").getCanonicalFile();
            if (directory.startsWith(current.getName()) && directory.equals(current.getName())) {
                directory = directory.substring(current.getName().length() + 1);
            }
        } catch (IOException ex) {
            // Ignore?
        }

        System.setProperty("vertx.cwd", directory);
        Consumer<Vertx> runner = vertx -> {
            try {
                if (deployment != null)
                    vertx.deployVerticle(id, deployment);
                else
                    vertx.deployVerticle(id);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        };

        if (options.getEventBusOptions().isClustered()) {
            Vertx.clusteredVertx(options, vertxAsyncResult -> {
                if (vertxAsyncResult.succeeded()) {
                    Vertx vertx = vertxAsyncResult.result();
                    runner.accept(vertx);
                } else {
                    vertxAsyncResult.cause().printStackTrace();
                }
            });
        } else {
            Vertx vertx = Vertx.vertx(options);
            runner.accept(vertx);
        }
    }
}
