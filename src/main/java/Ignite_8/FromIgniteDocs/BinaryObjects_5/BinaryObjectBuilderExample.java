package Ignite_8.FromIgniteDocs.BinaryObjects_5;

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
import org.apache.ignite.binary.BinaryObjectBuilder;
import org.apache.ignite.cache.CacheEntryProcessor;
import org.apache.ignite.configuration.CacheConfiguration;

import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;

public class BinaryObjectBuilderExample extends AbstractVerticle {

    /*
        This is used to update fields/create a new BinaryObject, because BO is immutable. (Which results in all
        kinds of personal hygiene jokes)

        BOB the Builder (Binary Object Builder... really?) is part of the IgniteBinary wrapper. The back door is
        by calling toBuilder() from a BO object
     */
    public static void main(String[] args) {
        setup();
    }

    @Override
    public void start() {
        try (Ignite ignite = Ignition.start()) {

            CacheConfiguration<Long, Person> cacheConfiguration = new CacheConfiguration<>();
            cacheConfiguration.setName("another-cache");

            try (IgniteCache<Long, Person> igniteCache = ignite.getOrCreateCache(cacheConfiguration)) {
                // Put something in it
                Long id = 1L;
                Person person = new Person(id, "Johnny", "BeGood");
                igniteCache.put(id, person);

                System.out.println("Before My Grubby Hands Get To it: " + igniteCache.get(id));

                igniteCache.<Long, BinaryObject>withKeepBinary()
                        .invoke(id, new CacheEntryProcessor<Long, BinaryObject, Object>() {
                            @Override
                            public Object process(MutableEntry<Long, BinaryObject> mutableEntry, Object... objects)
                                    throws EntryProcessorException {
                                BinaryObjectBuilder bobTheBuilder = mutableEntry.getValue().toBuilder();

                                // Update the field to something different, and then actually perform the change
                                bobTheBuilder.setField("lastName", "Rockets");
                                mutableEntry.setValue(bobTheBuilder.build());

                                return null;
                            }
                        });


                System.out.println("After Bob The Builder Gets To It: " + igniteCache.get(id));
            } finally {
                ignite.destroyCache("another-cache");
            }
        }

    }

    private static void setup() {
        // Just hiding the setup from main.

        ClusterManager clusterManager = new IgniteClusterManager();
        EventBusOptions eventBusOptions = new EventBusOptions().setClustered(true);
        VertxOptions vertxOptions = new VertxOptions()
                .setEventBusOptions(eventBusOptions)
                .setClusterManager(clusterManager);

        Consumer<Vertx> runner = consumerVertx -> {
            try {
                consumerVertx.deployVerticle(BinaryObjectBuilderExample.class.getName());
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
}
