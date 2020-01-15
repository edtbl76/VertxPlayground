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
import org.apache.ignite.binary.BinaryField;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.configuration.CacheConfiguration;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BinaryFieldExample extends AbstractVerticle {

    public static void main(String[] args) {
        setup();
    }

    @Override
    public void start() {

        try (Ignite ignite = Ignition.start()) {
            // Set up a cache
            CacheConfiguration<Long, Person> cacheConfiguration = new CacheConfiguration<>();
            cacheConfiguration.setName("this-line-is-offensive");


            try (IgniteCache<Long, Person> igniteCache = ignite.getOrCreateCache(cacheConfiguration)) {

                /*
                    Create a Map of Persons, and then add it to the cache
                 */
                Map<Long, Person> persons = new ConcurrentHashMap<>();
                persons = Map.of(
                        1L, new Person(1L, 1L, "Duane", "Brown", 100, null),
                        2L, new Person(2L, 2L, "Mike", "Iupati", 90, null),
                        3L, new Person(3L, 3L, "Justin", "Britt", 80, null),
                        4L, new Person(4L, 4L, "D.J.", "Fluker", 70, null),
                        5L, new Person(5L, 5L, "Germain", "Ifedi", 60, null)
                        );
                igniteCache.putAll(persons);

                // Convert Cache to Binary
                IgniteCache<Long, BinaryObject> binaryCache = ignite.cache(igniteCache.getName()).withKeepBinary();

                // Get the relevant Information
                Collection<BinaryObject> binaryPersons = binaryCache.getAll(persons.keySet()).values();

                BinaryField salary = null;
                double total = 0;
                int counter = 0;

                for (BinaryObject person : binaryPersons) {
                    System.out.println("BinaryExample   : " + person);
                    System.out.println("DeSerialExample : " + person.deserialize());
                    if (salary == null) {
                        salary = person.type().field("salary");
                    }

                    // The IDE barks about this cast, but if you don't put it in, the types don't match.
                    total += (double)salary.value(person);
                    counter++;
                }

                double average  = total / counter;
                System.out.println("Average Salary: " + average);



            } finally {
                ignite.destroyCache("this-cache");
            }
        }
    }

    private static void setup() {
        /*
            Set up The Cluster, EventBus and Vertx settings
         */
        ClusterManager clusterManager = new IgniteClusterManager();
        EventBusOptions eventBusOptions = new EventBusOptions().setClustered(true);
        VertxOptions vertxOptions = new VertxOptions()
                .setEventBusOptions(eventBusOptions)
                .setClusterManager(clusterManager);


        /*
            Use Rx Consumer to manage my verticle "Runner"
         */
        Consumer<Vertx> runner = consumerVertx -> {
            try {
                consumerVertx.deployVerticle(BinaryFieldExample.class.getName());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        };

        Vertx.clusteredVertx(vertxOptions, vertxAsyncResult -> {
            if (vertxAsyncResult.succeeded()) {
                Vertx vertx = vertxAsyncResult.result();
                try {
                    runner.accept(vertx);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                vertxAsyncResult.cause().printStackTrace();
            }
        });
    }



}
