package Ignite_8.FromIgniteDocs.ThreadPool_3;

import org.apache.ignite.Ignite;
import org.apache.ignite.lang.IgniteRunnable;
import org.apache.ignite.resources.IgniteInstanceResource;

public class OuterRunnable implements IgniteRunnable {

    @IgniteInstanceResource
    private Ignite ignite;

    @Override
    public void run() {
        /*
            This Synchronously executes my custom runnable in a custom executor.
         */
        ignite.compute().withExecutor("custom-pool").run(new InnerRunnable());
    }
}
