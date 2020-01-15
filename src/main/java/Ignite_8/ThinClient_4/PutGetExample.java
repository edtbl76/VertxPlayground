package Ignite_8.ThinClient_4;

import Util.IgniteModels.Address;
import Util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import org.apache.ignite.Ignition;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;

public class PutGetExample extends AbstractVerticle {
    public static void main(String[] args) {
        Runner.runClusteredExample(PutGetExample.class);
    }

    @Override
    public void start() {
        ClientConfiguration config = new ClientConfiguration().setAddresses("127.0.0.1:10800");

        try (IgniteClient igniteClient = Ignition.startClient(config)) {
            System.out.println("Thin client PutGet Example");

            // Create a cache.
            final String CACHE_NAME = "put-get-cache";
            ClientCache<Integer, Address> cache = igniteClient.getOrCreateCache(CACHE_NAME);
            System.out.println("Created cache [" + CACHE_NAME + "].");

            // Store something
            Integer key = 1;
            Address address = new Address("300 Riverpark Drive", 1842);
            cache.put(key, address);
            System.out.println("Wrote [" + address + "] to " + CACHE_NAME);

            // Get it out
            Address cachedAddress = cache.get(key);
            System.out.println("Read [" + cachedAddress + "] from " + CACHE_NAME);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

    }
}
