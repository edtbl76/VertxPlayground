package Ignite_8.FromIgniteDocs.ResourceInjection_6;

import Util.IgniteModels.Person;
import Util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.resources.IgniteInstanceResource;

public class MethodBasedInjectionExample extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.runClusteredExample(MethodBasedInjectionExample.class);
    }

    @Override
    public void start() {
        try (Ignite ignite = Ignition.start()) {
            Job job = new Job();
            job.setIgnite(ignite);   // <-- inject!
            job.execute();
        }
    }
}


class Job implements ComputeJob {
    private Ignite ignite;

    @IgniteInstanceResource
    public void setIgnite(Ignite ignite) {
        this.ignite = ignite;
    }

    @Override
    public void cancel() {

    }

    @Override
    public Object execute() throws IgniteException {
        CacheConfiguration<Long, Person> cacheConfiguration = new CacheConfiguration<>();
        cacheConfiguration.setName("injected-resource");

        try (IgniteCache<Long, Person> igniteCache = ignite.getOrCreateCache(cacheConfiguration)) {
            igniteCache.put(1L, new Person(1L, "Space", "Ghost"));

            System.out.println("Data in Cache: " + igniteCache.get(1L));

        } finally {
            ignite.destroyCache("injected-resource");
        }

        return null;
    }
}
