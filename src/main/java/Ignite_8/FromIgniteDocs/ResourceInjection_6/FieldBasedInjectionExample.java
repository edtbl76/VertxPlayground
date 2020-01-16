package Ignite_8.FromIgniteDocs.ResourceInjection_6;

import Util.IgniteModels.Person;
import Util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.lang.IgniteCallable;

import java.util.Collection;

public class FieldBasedInjectionExample extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.runClusteredExample(FieldBasedInjectionExample.class);
    }

    @Override
    public void start() {

        try (Ignite ignite = Ignition.start()) {

            CacheConfiguration<Long, Person> cacheConfiguration = new CacheConfiguration<>();
            cacheConfiguration.setName("dependency-injection-is-great-for-testing");

            Collection<String> result = ignite.compute()
                    .broadcast(new IgniteCallable<>() {

                        @Override
                        public String call() {
                            try (IgniteCache<Long, Person> igniteCache = ignite.getOrCreateCache(cacheConfiguration)) {
                                igniteCache.put(1L, new Person(1L, "Dudley", "Doright"));

                                System.out.println(igniteCache.get(1L));
                            }

                            return null;
                        }
                    });
        }
    }

}
