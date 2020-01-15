package ExecuteBlockingForDBs_4;

import Util.Runner;
import io.vertx.core.DeploymentOptions;

public class ExecBlockingExample2_UseDedicatedPool {

    public static void main(String[] args) {
        Runner.runExample(
                ExecuteBlockingExample.class,
                new DeploymentOptions()
                .setWorkerPoolName("my-dedicated-pool")
                .setMaxWorkerExecuteTime(120000)
                .setWorkerPoolSize(10)
        );
    }
}
