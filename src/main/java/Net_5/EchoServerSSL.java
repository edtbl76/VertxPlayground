package Net_5;

import Util.Runner;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.NetServerOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.streams.Pump;

import java.nio.file.Path;
import java.nio.file.Paths;

public class EchoServerSSL extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.runExample(EchoServerSSL.class);
    }

    @Override
    public void start() {
        NetServerOptions opts = new NetServerOptions()
                .setSsl(true)
                .setKeyStoreOptions(
                        new JksOptions()
                        .setPath(Paths.get("").toAbsolutePath()
                                + "/src/main/resources"
                                + "server-keystore.jks")
                        .setPassword("wibble"));

        vertx.createNetServer(opts).connectHandler(netSocket -> {
            Pump.pump(netSocket, netSocket).start();
        }).listen(1234);

        System.out.println("EchoServer Is Listening....");
    }
}
