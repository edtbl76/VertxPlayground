package Ignite_8.BinaryDataGrid_2;

import Util.IgniteModels.*;
import Util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.*;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.TextQuery;
import org.apache.ignite.configuration.CacheConfiguration;

import javax.cache.Cache;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BinaryIgniteCacheQueries extends AbstractVerticle {

    private static final String ORG_CACHE_NAME = BinaryIgniteCacheQueries.class.getSimpleName() + "Orgs";
    private static final String EMP_CACHE_NAME = BinaryIgniteCacheQueries.class.getSimpleName() + "Employees";

    public static void main(String[] args) {
        Runner.runClusteredExample(BinaryIgniteCacheQueries.class);
    }

    @Override
    public void start() {
        try (Ignite ignite = Ignition.start()) {
            System.out.println("Binary Objects Cache Query Example: ");

            CacheConfiguration<Integer, Org> orgCacheConfiguration = new CacheConfiguration<>();
            orgCacheConfiguration
                    .setCacheMode(CacheMode.PARTITIONED)
                    .setName(ORG_CACHE_NAME)
                    .setQueryEntities(Collections.singletonList(createOrgQueryEntity()));

            CacheConfiguration<EmployeeKey, Employee> employeeCacheConfiguration = new CacheConfiguration<>();
            employeeCacheConfiguration
                    .setCacheMode(CacheMode.PARTITIONED)
                    .setName(EMP_CACHE_NAME)
                    .setQueryEntities(Collections.singletonList(createEmpQueryEntity()))
                    .setKeyConfiguration(new CacheKeyConfiguration(EmployeeKey.class));

            try (IgniteCache<Integer, Org> orgCache = ignite.getOrCreateCache(orgCacheConfiguration);
                 IgniteCache<EmployeeKey, Employee> empCache = ignite.getOrCreateCache(employeeCacheConfiguration)) {

                if (ignite.cluster().forDataNodes(orgCache.getName()).nodes().isEmpty()) {
                    System.out.println("Create a remote cache node.");
                }

                seedCache(orgCache, empCache);

                IgniteCache<BinaryObject, BinaryObject> binCache = empCache.withKeepBinary();
                queryFields(binCache);
                queryJoin(binCache);
                queryText(binCache);
            } finally {
                ignite.destroyCaches(Arrays.asList(ORG_CACHE_NAME, EMP_CACHE_NAME));
            }
        }
    }

    private static QueryEntity createEmpQueryEntity() {
        return new QueryEntity()
                .setValueType(Employee.class.getName())
                .setKeyType(EmployeeKey.class.getName())
                .addQueryField("orgId", Integer.class.getName(), null)
                .addQueryField("name", String.class.getName(), null)
                .addQueryField("salary", Long.class.getName(), null)
                .addQueryField("addr.zip", Integer.class.getName(), null)
                .addQueryField("addr.street", String.class.getName(), null)
                .setKeyFields(Collections.singleton("orgId"))
                .setIndexes(Arrays.asList(
                        new QueryIndex("name"),
                        new QueryIndex("salary"),
                        new QueryIndex("addr.zip"),
                        new QueryIndex("orgId"),
                        new QueryIndex("addr.street", QueryIndexType.FULLTEXT)
                ));
    }

    private static QueryEntity createOrgQueryEntity() {

        return new QueryEntity()
                .setValueType(Org.class.getName())
                .setKeyType(Integer.class.getName())
                .addQueryField("keyId", Integer.class.getName(), null)
                .addQueryField("name", String.class.getName(), null)
                .addQueryField("address.street", String.class.getName(), null)
                .setKeyFieldName("keyId")
                .setIndexes(Collections.singletonList(new QueryIndex("name")));
    }

    private static void queryFields(IgniteCache<BinaryObject, BinaryObject> igniteCache) {
        SqlFieldsQuery query = new SqlFieldsQuery("select name, salary from Employee");
        QueryCursor<List<?>> employees = igniteCache.query(query);
        System.out.println("Fields Query Example: ");
        for (List<?> row : employees.getAll())
            System.out.println("Name: " + row.get(0) + ", Salary: " + row.get(1));
    }

    private static void queryJoin(IgniteCache<BinaryObject, BinaryObject> igniteCache) {
        SqlFieldsQuery query = new SqlFieldsQuery(
                "select emp.* from Employee emp, \"" + ORG_CACHE_NAME + "\".Org as org " +
                        "where emp.orgId = org.keyId and org.name = ?");

        String orgName = "TraceLink";
        QueryCursor<List<?>> employees = igniteCache.query(query.setArgs(orgName));

        System.out.println("Join Query Example: ");
        for(List<?> row : employees.getAll())
            System.out.println("\t" + row);
    }

    private static void queryText(IgniteCache<BinaryObject, BinaryObject> igniteCache) {
        TextQuery<BinaryObject, BinaryObject> query = new TextQuery<>(Employee.class, "Tatooine");
        QueryCursor<Cache.Entry<BinaryObject, BinaryObject>> employees = igniteCache.query(query);

        System.out.println("Text Query Example: ");
        for (Cache.Entry<BinaryObject, BinaryObject> entry : employees.getAll())
            System.out.println("\t" + entry.getValue().deserialize());
    }

    private static void seedCache(
            IgniteCache<Integer, Org> orgIgniteCache, IgniteCache<EmployeeKey,
            Employee> empIgniteCache) {


            orgIgniteCache.putAll(getTwoOrgs());
            empIgniteCache.putAll(getEmployees());
    }

    /*
        Yes this code sucks. It's just for demo purposes.
     */
    private static Map<Integer, Org> getTwoOrgs() {
        return  Map.of(

                // Entry 1
                1, new Org(
                "TraceLink",
                new Address("300 Riverpark Drive", 1864),
                OrgType.PRIVATE,
                new Timestamp(System.currentTimeMillis())),

                // Entry 2
                2, new Org(
                "Brand X",
                new Address("Uncomfortable Office", 1864),
                OrgType.PRIVATE,
                new Timestamp(System.currentTimeMillis()))
        );
    }

    private static Map<EmployeeKey, Employee> getEmployees() {
        return Map.of(
                new EmployeeKey(1, 1),
                new Employee(
                        "Luke Skywalker",
                        1000000,
                        new Address("Tatooine", 99999),
                        Arrays.asList("Engineering", "Jedi Knights")
                ),

                new EmployeeKey(2, 2),
                new Employee(
                        "Darth Vader",
                        1000,
                        new Address("Tatooine", 99999),
                        Arrays.asList("Mailroom", "Sith Lord")
                ),

                new EmployeeKey(3, 1),
                new Employee(
                        "Han Solo",
                        100000,
                        new Address("Corellia", 88888),
                        Arrays.asList("Pilot", "Uber Driver")
                ),

                new EmployeeKey(4, 1),
                new Employee(
                        "Yoda",
                        0,
                        new Address("Degobah", 77777),
                        Arrays.asList("TechPubs", "FrontDesk")
                )
        );
    }
}
