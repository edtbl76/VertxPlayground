package Composition_3;

import Util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Future;
import io.vertx.reactivex.core.Promise;

public class Composition  extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.runExample(Composition.class);
    }

    @Override
    public void start() throws Exception {
        Future<String> future = asyncAction1();
        future.compose(this::asyncAction2).setHandler(
                objectAsyncResult -> {
                   if (objectAsyncResult.succeeded())
                       System.out.println("Result: " + objectAsyncResult.result());
                   else {
                       System.out.println("Stuff Happens");
                       objectAsyncResult.cause().printStackTrace();
                   }
                });
    }

    private Future<String> asyncAction1() {
        Promise<String> promise = Promise.promise();
        vertx.setTimer(100, aLong -> promise.complete("world"));
        return promise.future();
    }

    private Future<String> asyncAction2(String str) {
        Promise<String> promise = Promise.promise();
        vertx.setTimer(100, aLong -> promise.complete("hello " + str));
        return promise.future();
    }
}
