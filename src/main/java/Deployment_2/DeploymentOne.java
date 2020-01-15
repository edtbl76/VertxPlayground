package Deployment_2;

import Util.Runner;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;

public class DeploymentOne extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.runExample(DeploymentOne.class);
    }

    @Override
    public void start() throws Exception {

        System.out.println("Deployment Verticle Started. Deploying OTHER Verticles");

        // Deploy asynchronously
        vertx.deployVerticle(VerticleTemplate.class.getName());

        // Deploy synchronously
        vertx.deployVerticle(VerticleTemplate.class.getName(), stringAsyncResult -> {
            if (stringAsyncResult.succeeded()) {
                String id = stringAsyncResult.result();
                System.out.println("Deployment Succeeded for Verticle: " + id);

                // Explicit Undeploy
                vertx.undeploy(id, voidAsyncResult -> {
                    if (voidAsyncResult.succeeded()) {
                        System.out.println("Undeploy Succeeded for Verticle: " + id);
                    } else {
                        voidAsyncResult.cause().printStackTrace();
                    }
                });
            } else {
                stringAsyncResult.cause().printStackTrace();
            }
        });

        // Deployments using Non-Default Configuration Entries
        JsonObject config = new JsonObject().put("Data", "Persistence");
        vertx.deployVerticle(VerticleTemplate.class.getName(), new DeploymentOptions().setConfig(config));

        // Multi reactor
        vertx.deployVerticle(VerticleTemplate.class.getName(), new DeploymentOptions().setInstances(5));
        // Set Worker
        vertx.deployVerticle(VerticleTemplate.class.getName(), new DeploymentOptions().setWorker(true));
    }
}
