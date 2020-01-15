package Ignite_8.BinaryDataGrid_2;

import Util.IgniteModels.Address;
import Util.IgniteModels.Org;
import Util.IgniteModels.OrgType;
import Util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BinaryIgniteCache extends AbstractVerticle {

    private static final String CACHE_NAME = BinaryIgniteCache.class.getSimpleName();

    public static void main(String[] args) {
        Runner.runClusteredExample(BinaryIgniteCache.class);
    }

    @Override
    public void start() {
        try (Ignite ignite = Ignition.start()) {
            System.out.println("\n Example Start >>> ");

            CacheConfiguration<Integer, Org> cacheConfiguration = new CacheConfiguration<>();
            cacheConfiguration.setCacheMode(CacheMode.PARTITIONED);
            cacheConfiguration.setName(CACHE_NAME);
            cacheConfiguration.setAtomicityMode(CacheAtomicityMode.ATOMIC);

            try (IgniteCache<Integer, Org> cache = ignite.getOrCreateCache(cacheConfiguration)) {
                if (ignite.cluster().forDataNodes(cache.getName()).nodes().isEmpty()) {
                    System.out.println("You need at least one remote cache node");
                }

                putGet(cache);
                putGetBinary(cache);
                putGetAll(cache);
                putGetAllBinary(cache);
            }
            finally {
                ignite.destroyCache(CACHE_NAME);
            }
        }
    }

    // Normal Put + Get
    private static void putGet(IgniteCache<Integer, Org> igniteCache) {
        Org org = new Org(
                "TraceLink",
                new Address("300 Riverpark Drive", 1864),
                OrgType.PRIVATE,
                new Timestamp(System.currentTimeMillis()));

        igniteCache.put(1, org);
        Org orgFromCache = igniteCache.get(1);
        System.out.println("PutGet: " + orgFromCache);
    }

    /*
        put + get in Binary format. (no deserialization required)
     */
    private static void putGetBinary(IgniteCache<Integer, Org> igniteCache) {
        Org org = getOneOrg();

        igniteCache.put(1, org);
        IgniteCache<Integer, BinaryObject> biCache = igniteCache.withKeepBinary();
        BinaryObject result = biCache.get(1);

        /*
            cherry pick the data we want to deserialize
         */
        String name = result.field("name");
        System.out.println("PutGetBinary Example: " + name);
    }

    /*
        Normal put/get Batch
     */
    private static void putGetAll(IgniteCache<Integer, Org> igniteCache) {
        List<Org> orgs = getTwoOrgs();

        Map<Integer, Org> map = new ConcurrentHashMap<>();
        map.put(1, orgs.get(0));
        map.put(2, orgs.get(1));

        igniteCache.putAll(map);

        Map<Integer, Org> resultMap = igniteCache.getAll(map.keySet());

        System.out.println("PutGetAll Results: ");
        for (Org org : resultMap.values()) {
            System.out.println("\t" + org);
        }
    }

    /*
        Bulk operations w/o needing to de-serialize the whole thing.
     */
    private static void putGetAllBinary(IgniteCache<Integer, Org> igniteCache) {
        List<Org> orgs = getTwoOrgs();

        Map<Integer, Org> map = new ConcurrentHashMap<>();
        map.put(1, orgs.get(0));
        map.put(2, orgs.get(1));
        igniteCache.putAll(map);

        IgniteCache<Integer, BinaryObject> biCache = igniteCache.withKeepBinary();
        Map<Integer, BinaryObject> resultMap = biCache.getAll(map.keySet());

        System.out.println("PutGetAllBinary Results: ");
        for (BinaryObject bo : resultMap.values()) {
            System.out.println("\t" + bo.field("name").toString());
        }

    }

    private static Org getOneOrg() {
        return new Org(
                "TraceLink",
                new Address("300 Riverpark Drive", 1864),
                OrgType.PRIVATE,
                new Timestamp(System.currentTimeMillis()));
    }

    private static List<Org> getTwoOrgs() {
        return Arrays.asList(
                new Org(
                        "TraceLink",
                        new Address("300 Riverpark Drive", 1864),
                        OrgType.PRIVATE,
                        new Timestamp(System.currentTimeMillis())),
                new Org(
                        "Brand X",
                        new Address("Uncomfortable Office", 1864),
                        OrgType.PRIVATE,
                        new Timestamp(System.currentTimeMillis()))
        );
    }

}
