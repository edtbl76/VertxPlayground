package Ignite_8.ClusterGroup_5;

import Util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCluster;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterGroup;

public class Clustering extends AbstractVerticle {
    public static void main(String[] args) {
        Runner.runClusteredExample(Clustering.class);
    }

    @Override
    public void start() {
        try (Ignite ignite = Ignition.start()) {

            System.out.println("Compute Example Start:");
            IgniteCluster cluster = ignite.cluster();

            // Broadcasts to all nodes in cluster.
            broadcast(ignite, cluster);

            // Remote nodes (i.e. all but me)
            broadcast(ignite, cluster.forRemotes());

            // Select random node
            ClusterGroup random = cluster.forRemotes().forRandom();

            // Broadcast to random
            broadcast(ignite, random);

            // Broadcast to all nodes on  same host w/random node.
            broadcast(ignite, cluster.forHost(random.node()));

            // broadcast to all nodes w/ CPU load < 50%
            broadcast(ignite, cluster.forPredicate(clusterNode -> clusterNode.metrics().getCurrentCpuLoad() < 0.5));

        }
    }

    private static void broadcast(Ignite ignite, final ClusterGroup clusterGroup) {
        ignite.compute(clusterGroup).broadcast(
                () -> System.out.println("Hello: " + clusterGroup.ignite().cluster().localNode().id()));
    }
}
