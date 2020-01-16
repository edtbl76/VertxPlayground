package Ignite_8.FromIgniteDocs.Logging_7;

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
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.logger.slf4j.Slf4jLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LoggingExample extends AbstractVerticle {

    static IgniteConfiguration igniteConfiguration = new IgniteConfiguration();

    public static void main(String[] args) {
        vertSetup();
    }

    @Override
    public void start() {

        try (Ignite ignite = Ignition.start(igniteConfiguration)) {
            System.out.println("\n\n\n\n\n\nMY LOG MESSAGES\n\n\n\n\n\n");
            ignite.log().info("LogFile:       " + ignite.log().fileName() +
                    "\nDebugEnabled: " + ignite.log().isDebugEnabled() +
                    "\nInfoEnabled : " + ignite.log().isInfoEnabled() +
                    "\nIsQuiet     : " + ignite.log().isQuiet() +
                    "\nTraceEnabled: " + ignite.log().isTraceEnabled());
            System.out.println("\n\n\n\n\n\nMY LOG MESSAGES\n\n\n\n\n\n");

//            InputStream is = LoggingExample.class.getClassLoader().getResourceAsStream("igni);
//            BufferedReader br = new BufferedReader(new InputStreamReader(is));
//
//            String line;
//            while ((line = br.readLine()) != null) {
//                System.out.println(line + System.lineSeparator());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
        }
    }

    private static void vertSetup() {
        /*
            Logging Setup
         */

        IgniteLogger igniteLogger = new Slf4jLogger();

        igniteConfiguration.setGridLogger(igniteLogger);

        ClusterManager clusterManager = new IgniteClusterManager();
        EventBusOptions eventBusOptions = new EventBusOptions().setClustered(true);
        VertxOptions vertxOptions = new VertxOptions()
                .setEventBusOptions(eventBusOptions)
                .setClusterManager(clusterManager);

        Consumer<Vertx> runner = consumerVertx -> {
            try {
                consumerVertx.deployVerticle(LoggingExample.class.getName());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        };

        Vertx.clusteredVertx(vertxOptions, vertxAsyncResult -> {
            if (vertxAsyncResult.succeeded()) {
                try {
                    runner.accept(vertxAsyncResult.result());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                vertxAsyncResult.cause().printStackTrace();
            }
        });






    }
}
