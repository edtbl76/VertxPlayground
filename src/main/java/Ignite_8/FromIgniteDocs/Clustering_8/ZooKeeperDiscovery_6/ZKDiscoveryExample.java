package Ignite_8.FromIgniteDocs.Clustering_8.ZooKeeperDiscovery_6;

import io.reactivex.functions.Consumer;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.spi.cluster.ignite.IgniteClusterManager;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.zk.ZookeeperDiscoverySpi;

public class ZKDiscoveryExample extends AbstractVerticle {

    static IgniteConfiguration igniteConfiguration = new IgniteConfiguration();

    public static void main(String[] args)  {
        ClusterManager  clusterManager = new IgniteClusterManager();
        EventBusOptions eventBusOptions = new EventBusOptions().setClustered(true);
        VertxOptions vertxOptions = new VertxOptions()
                .setEventBusOptions(eventBusOptions)
                .setClusterManager(clusterManager);

        Consumer<Vertx> runner = cv -> {
            try {
                cv.deployVerticle(ZKDiscoveryExample.class.getName());
            } catch (Throwable  throwable) {
                throwable.printStackTrace();
            }
        };

        Vertx.clusteredVertx(vertxOptions, event -> {
            if (event.succeeded()) {
                try {
                    runner.accept(event.result());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                event.cause().printStackTrace();
            }
        });
    }


    @Override
    public void start() {

        ZookeeperDiscoverySpi zkDiscoverySpi = new ZookeeperDiscoverySpi();
        zkDiscoverySpi
                .setZkConnectionString("127.0.0.1:34076,127.0.0.1:43310,127.0.0.1:36745")
                .setSessionTimeout(30000)
                .setZkRootPath("/Users/emangini/zookeeper/")
                .setJoinTimeout(10000);

        igniteConfiguration.setDiscoverySpi(zkDiscoverySpi);


        try (Ignite ignite = Ignition.start(igniteConfiguration)) {
            System.out.println("Hello World!");
        }
    }
}
