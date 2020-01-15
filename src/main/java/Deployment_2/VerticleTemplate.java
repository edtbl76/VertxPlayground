package Deployment_2;

import io.vertx.reactivex.core.AbstractVerticle;

public class VerticleTemplate extends AbstractVerticle {

    @Override
    public void start() {
        System.out.println("VerticleTemplate Started.");
        System.out.println("Config is " + config());
    }

    @Override
    public void stop() {
        System.out.println("VerticleTemplate Stopped");
    }
}
