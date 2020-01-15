package Ignite_8.FromIgniteDocs.ThreadPool_3;

import org.apache.ignite.lang.IgniteRunnable;

public class InnerRunnable implements IgniteRunnable {
    @Override
    public void run() {
       System.out.println("Greetings Program: Your Inner Program");
    }
}
