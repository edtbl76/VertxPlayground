package Ignite_8.FromIgniteDocs.BinaryObjects_5;

import Util.IgniteModels.Org;
import Util.IgniteModels.Person;
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
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

public class BinObjExample extends AbstractVerticle {

    public static void main(String[] args) {
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        ClusterManager clusterManager = new IgniteClusterManager();
        EventBusOptions eventBusOptions = new EventBusOptions().setClustered(true);
        VertxOptions vertxOptions = new VertxOptions()
                .setEventBusOptions(eventBusOptions)
                .setClusterManager(clusterManager);

        Consumer<Vertx> runner = consumerVertx -> {
            try {
                consumerVertx.deployVerticle(BinObjExample.class.getName());
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

        try (Ignite ignite = Ignition.start()) {
            CacheConfiguration<Long, Person> cacheConfiguration = new CacheConfiguration<>();
            cacheConfiguration.setCacheMode(CacheMode.PARTITIONED);
            cacheConfiguration.setName("this-cache");
            cacheConfiguration.setAtomicityMode(CacheAtomicityMode.ATOMIC);

            try (IgniteCache<Long, Person> igniteCache = ignite.getOrCreateCache(cacheConfiguration)) {

                Long personId = 1L;
                Person person = new Person(personId, "Ed", "Mangini");
                igniteCache.put(personId, person);

                IgniteCache<Long, BinaryObject> binaryCache = ignite.cache(igniteCache.getName()).withKeepBinary();
                BinaryObject binaryObject = binaryCache.get(personId);
                System.out.println("Result: [" + binaryObject + "]");
                System.out.println("Deserialized: " + binaryObject.field("firstName"));

            } finally {
                ignite.destroyCache("this-cache");
            }
        }
    }
}
