package HTTP_6;

import Util.Runner;
import io.vertx.core.DeploymentOptions;
import io.vertx.reactivex.core.AbstractVerticle;

public class SharingServer extends AbstractVerticle {
    public static void main(String[] args) {
        Runner.runExample(SharingServer.class);
    }

    @Override
    public void start() {
        vertx.deployVerticle(
                SimpleHTTPServerV2.class.getName(),
                new DeploymentOptions().setInstances(2)
        );

    }
}
