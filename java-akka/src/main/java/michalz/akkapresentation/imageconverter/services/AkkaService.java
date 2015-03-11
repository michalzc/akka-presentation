package michalz.akkapresentation.imageconverter.services;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import michalz.akkapresentation.imageconverter.domain.protocol.InitializeGateway;
import michalz.akkapresentation.imageconverter.domain.protocol.JobStarted;
import michalz.akkapresentation.imageconverter.domain.protocol.StoreImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by michal on 11.03.15.
 */
@Service
public class AkkaService {

    @Autowired
    private ApplicationContext applicationContext;

    private ActorSystem actorSystem;
    private ActorRef gatewayRef;

    private static Timeout DEFAULT_TIMEOUT = new Timeout(Duration.create(1, "seconds"));


    @PostConstruct
    public void init() {
        actorSystem = ActorSystem.create("java-app-actor-system");
        gatewayRef = actorSystem.actorOf(GatewayActor.props(applicationContext), "gateway");
        gatewayRef.tell(new InitializeGateway(), ActorRef.noSender());
    }

    @PreDestroy
    public void shutDown() {
        actorSystem.shutdown();
        actorSystem.awaitTermination(Duration.create(5, "seconds"));
    }

    public JobStarted storeImage(byte[] imageData) throws Exception {
        Future<Object> storeFuture = Patterns.ask(gatewayRef, new StoreImage(imageData), DEFAULT_TIMEOUT);
        return (JobStarted) Await.result(storeFuture, DEFAULT_TIMEOUT.duration());
    }
}
