package Net_5;

import Util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.parsetools.RecordParser;

import java.nio.charset.StandardCharsets;

public class RPServer extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.runExample(RPServer.class);
    }

    @Override
    public void start() {

        vertx.createNetServer().connectHandler(netSocket -> {

            RecordParser parser = RecordParser.newDelimited("\n", netSocket);

            parser
                    .endHandler(v -> netSocket.close())
                    .exceptionHandler(throwable -> {
                        throwable.printStackTrace();
                        netSocket.close();
                    })
                    .handler(buffer -> {
                        netSocket.write("Hello " + buffer.toString(StandardCharsets.UTF_8) + "\n", "UTF-8");
                    });
        }).listen(1234);

        System.out.println("RecordParser Server is Listening...");
    }
}
