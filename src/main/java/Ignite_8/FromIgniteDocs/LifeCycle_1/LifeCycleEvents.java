package Ignite_8.FromIgniteDocs.LifeCycle_1;

import Util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lifecycle.LifecycleBean;
import org.apache.ignite.lifecycle.LifecycleEventType;

public class LifeCycleEvents extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.runClusteredExample(LifeCycleEvents.class);
    }

    @Override
    public void start() {
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setLifecycleBeans(new LCBean());

        try (Ignite ignite = Ignition.start(cfg)) {
        }
    }
}

class LCBean implements LifecycleBean {
    @Override
    public void onLifecycleEvent(LifecycleEventType lifecycleEventType) throws IgniteException {
        if (lifecycleEventType == LifecycleEventType.BEFORE_NODE_START)
            System.out.println("\n\n\nBEFORE NODE START\n\n\n");
        else if (lifecycleEventType == LifecycleEventType.AFTER_NODE_START)
            System.out.println("\n\n\nNODE HAS BEEN STARTED\n\n\n");
        else if (lifecycleEventType == LifecycleEventType.BEFORE_NODE_STOP)
            System.out.println("\n\n\nNODE IS GOING TO BE STOPPED\n\n\n");
        else
            System.out.println("\n\n\nNODE IS STOPPED\n\n\n");
    }
}
