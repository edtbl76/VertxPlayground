package Ignite_8.FromIgniteDocs.ClientServer_2;

import Util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;

public class igniteServerExample extends AbstractVerticle {
    public static void main(String[] args) {
        Runner.runClusteredExample(igniteServerExample.class);
    }

    @Override
    public void start() {
        CacheConfiguration<String, String> config = new CacheConfiguration<>("my-cache");

        igniteClientServer(config);
    }

    private static void igniteClientServer(CacheConfiguration<String, String> config) {

        try(Ignite ignite = Ignition.start()) {
            IgniteCache<String, String> igniteCache = ignite.getOrCreateCache(config);


            /*
                client mode is defined in logging, so this code isn't necessary.
                I left it here as a reminder that we can accomplish this programmatically.
             */
//            if (Ignition.isClientMode()) {
//                System.out.println("\n\n\nI'm a client\n\n\n");
//            } else {
//                System.out.println("\n\n\nI'm a server\n\n\n");
//            }

            System.out.println("[" + igniteCache.getName() + "] " + "Empty Cache -> ");
            igniteCache.forEach(System.out::println);
            System.out.println("<- Empty Cache [" + igniteCache.getName() + "]");

            IgniteCompute igniteCompute = ignite.compute();
            igniteCompute.broadcast(() -> System.out.println("Thank you for flying with Apache Ignite!"));
        }
    }

}
