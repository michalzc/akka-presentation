package michalz.akkapresentation.imageconverter.services;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import michalz.akkapresentation.imageconverter.domain.protocol.*;
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
        gatewayRef.tell(new InitializeGatewayReq(), ActorRef.noSender());
    }

    @PreDestroy
    public void shutDown() {
        actorSystem.shutdown();
        actorSystem.awaitTermination(Duration.create(5, "seconds"));
    }

    public JobStartedResp storeImage(byte[] imageData) throws Exception {
        Future<Object> storeFuture = Patterns.ask(gatewayRef, new StoreImageReq(imageData), DEFAULT_TIMEOUT);
        return (JobStartedResp) Await.result(storeFuture, DEFAULT_TIMEOUT.duration());
    }

    public JobStatusResp getJobStatus(String jobId) {
        return null;
    }
}
