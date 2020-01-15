package Ignite_8.NodeStartup_1;

import Util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import org.apache.ignite.Ignition;

public class IgniteVerticle extends AbstractVerticle {
    public static void main(String[] args) {
        Runner.runClusteredExample(IgniteVerticle.class);
    }

    @Override
    public void start() {
        Ignition.start();
    }
}
